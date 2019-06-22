/**************************************************** RLP - EXERCISE **************************************************/
create or replace view rlp_weekly_exercise as (
    select date_trunc('week', date + interval '1 day') - interval '1 day' as week,
        sum(cardio) as cardio,
        sum(lifting) as lifting,
        sum(total) as total,
        case when sum(cardio) is null or sum(lifting) is null
                 then sum(total) / 7
             else
                     least(2::float / 3, 2::float / 3 * sum(cardio) / 3.5) +
                     least(1::float / 3, 1::float / 3 * sum(lifting) / 2.0) end as percentage
    from health
    group by week
    order by week desc)
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

/************************************************** RLP - EDUCATION ***************************************************/
create or replace view rlp_weekly_education as (
    select date_trunc('week', created_at + interval '1 day') - interval '1 day' as week,
        sum(total_minutes / 7) as average_minutes,
        sum(complete) as days_completed
    from (select *,
              case when total_minutes >= 14.5 or total_reviews >= 145 then 1 else 0 end as complete
          from (select date_trunc('day', created_at) as created_at,
                    (sum(time_ms) / 60000) as total_minutes,
                    count(time_ms) as total_reviews
                from anki_reviews
                group by 1) reviews_per_day) b
    group by week
    order by week DESC)
;


/************************************************* RLP - PRODUCTIVITY *************************************************/
create or replace view rlp_weekly_productivity as (
    with value_overrides as (select 'Toodledo Tasks' as title, 10 as override
                             union
                             select 'M+', 0.1
                             union
                             select 'N+', 0.1),
        summarized as (select date,
                           toodledo_habits.title,
                           added,
                           value * coalesce(override, 1) as contribution,
                           coalesce(override, 1) as outof
                       from toodledo_habit_repetitions
                                join toodledo_habits
                       on toodledo_habit_repetitions.habit = toodledo_habits.id
                                full outer join value_overrides
                       on toodledo_habits.title like '%' || value_overrides.title || '%'
                       where toodledo_habits.title like '%⭐%')
    select date_trunc('week', date + interval '1 day') - interval '1 day' as week,
        sum(contribution) as contrib,
        sum(outof) as outof,
        sum(contribution)::float / sum(outof) as percentage
    from summarized
    group by week
    order by week desc)
;


/* Completed Toodledo non-recurring tasks in last week */
select distinct title, completed_at
from toodledo_tasks
where title not in (select distinct title from toodledo_tasks where repeat != '') and
    completed_at >= current_date - 30 and title not like 'Update Lifting Stats' and
    title not like 'Workout overrides' and title not like 'Update events in yearly candlestick calendar' and
    title not like 'Vacuum car'
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
