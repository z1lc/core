/**************************************************** RLP - EXERCISE **************************************************/
create or replace view rlp_daily_exercise as (
    select date, case when total > 0 then 1 else 0 end as exercise
    from health
    order by date desc
)
;

create or replace view rlp_weekly_exercise as (
    select date_trunc('week', date + interval '1 day') - interval '1 day' as week,
        sum(cardio) as cardio,
        sum(lifting) as lifting,
        sum(total) as total,
        case when sum(cardio) is null or sum(lifting) is null
                 then sum(total) / 7
             else
                     least(2::float / 3, 2::float / 3 * sum(cardio) / 2.5) +
                     least(1::float / 3, 1::float / 3 * sum(lifting) / 2.0) end as percentage
    from health
    group by week
    order by week desc)
;


/************************************************** RLP - EDUCATION ***************************************************/
create or replace view rlp_daily_education as (
    select *,
        case when total_minutes >= 14.5 or total_reviews >= 145 then 1 else 0 end as complete
    from (select date_trunc('day', created_at) as created_at,
              (sum(time_ms) / 60000) as total_minutes,
              count(time_ms) as total_reviews
          from anki_reviews
          group by 1) reviews_per_day
    order by created_at desc
)
;

create or replace view rlp_weekly_education as (
    select date_trunc('week', created_at + interval '1 day') - interval '1 day' as week,
        sum(total_minutes / 7) as average_minutes,
        sum(complete) as days_completed
    from rlp_daily_education
    group by week
    order by week DESC)
;


/************************************************* RLP - PRODUCTIVITY *************************************************/
create or replace view rlp_daily_productivity as (
    with toodledo as (select date_trunc('day', completed_at) as day, sum(length_minutes) as sum_toodledo
                      from toodledo_tasks
                      where completed_at is not null
                      group by 1
                      order by 1 desc),
        habitica as (
            select date_trunc('day', date) as day, sum(time_in_minutes) as sum_habitica
            from habitica_tasks
                     join habitica_histories hh on habitica_tasks.id = hh.task_id
            where hh.completed
            group by 1
            order by 1 desc)
    select day, coalesce(sum_toodledo, 0) + coalesce(sum_habitica, 0) as total_minutes
    from toodledo
             full outer join habitica using (day)
)
;

create or replace view rlp_weekly_productivity as (
    select date_trunc('week', day + interval '1 day') - interval '1 day' as week,
        sum(case when total_minutes >= 30 then 1 else 0 end) as days_completed
    from rlp_daily_productivity
    group by 1
    order by 1 desc)
;


/***************************************************** RLP - SLEEP ****************************************************/
create or replace view rlp_weekly_sleep as (
    select date_trunc('week', date_of_sleep + interval '1 day') - interval '1 day' as week,
        stddev(minutes) as standard_deviation,
        case when stddev(minutes) <= 10
                 then 1
             when stddev(minutes) >= 60
                 then 0
             else 1 - greatest(stddev(minutes) - 10, 0::float) / 60 end as rating,
        count(*) as individual_sleep_logs
    from (select date_of_sleep,
              extract(hour from end_time) * 24 + extract(minute from end_time) as minutes
          from fitbit_sleep
          where time_in_bed >= 180
          order by date_of_sleep desc) as hours
    group by week
    order by week desc)
;


/************************************************* RLP - DAILY OVERALL ************************************************/
create or replace view rlp_daily as (
    select date(coalesce(date, created_at, day)) as day,
        ex.exercise as exercise,
        coalesce(ed.complete, 0) as education,
        case when p.total_minutes >= 30 then 1 else 0 end as productivity
    from rlp_daily_exercise ex
             full outer join rlp_daily_education ed on ex.date = ed.created_at
             full outer join rlp_daily_productivity p on ed.created_at = p.day
)
;

/* Recently-completed non-recurring Toodledo tasks */
select distinct title, completed_at
from toodledo_tasks
where title not in (select distinct title from toodledo_tasks where repeat != '') and
    completed_at >= '2019-09-18' and title not like 'Update Lifting Stats' and
    title not like 'Workout overrides' and title not like 'Update events in yearly candlestick calendar' and
    title not like 'Vacuum car' and title not like 'Change bedding %' and title not like 'Greylist inbox zero'
order by completed_at DESC
;

/* Anki time spend by tag */
select round(sum(time_ms) / 3.6e+6, 2) as hours
from anki_notes
         JOIN anki_cards on anki_notes.id = anki_cards.note_id
         JOIN anki_reviews on anki_cards.id = anki_reviews.card_id
--where tags LIKE '%z::Languages::Spanish%'
--where tags LIKE '%General_Knowledge::The_Office%'
where tags LIKE '%z::Computer_Science::Interview_Prep%'
;


/* ETL runs ordered by descending time */
select *,
    round(cast(extract(epoch from end_time - start_time) as numeric)) as seconds
from etl_runs
order by seconds DESC
;

/* get top wikipedia people URLs not yet in Anki */
select 'https://en.wikipedia.org/wiki/' || wikipedia_url_title
from wikipedia_people
where found_in_anki is false
order by rank asc
;

select cards.last_activity, cards.name, cards.description, lists.name
from trello_cards cards
         join trello_boards boards on cards.board_id = boards.id
         join trello_lists lists on lists.board_id = boards.id and lists.id = cards.list_id
where boards.name = 'Backlogs' and cards.closed is true
order by 1 desc
