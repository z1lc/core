create or replace view rlp_dates as
(
SELECT description, EXTRACT(epoch from date) as epoch, date
FROM (
    VALUES ('Last Year', current_date - 365),
        ('Last 5 Years', current_date - 1826),
        ('Last Decade', current_date - 3652),
        ('Since Starting at Stripe', date '10-21-2019'),
        ('Since Starting Sabbatical', date '03-22-2018'),
        ('Since Moving to San Mateo', date '03-18-2017'),
        ('Since Moving to Redwood City', date '03-25-2016'),
        ('Since Moving to California', date '08-01-2015'),
        ('Since Discovery of Anki', date '05-11-2014'),
        ('Since Starting College', date '08-22-2011'),
        ('Since Discovery of RescueTime', date '01-01-2009')
) AS temptable(description, date))
;

/**************************************************** RLP - EXERCISE **************************************************/
create or replace view rlp_daily_exercise as
(
select date, total
from health
order by date desc
    )
;

create or replace view rlp_weekly_exercise as
(
select date_trunc_week_sunday(h.date) as week,
    avg(case when fairly_active_minutes + very_active_minutes > 10 then 1 else 0 end)::float as cardio_percent,
    avg(case when lifting > 0 then 1 else 0 end) as lifting_percent,
    sum(fairly_active_minutes + very_active_minutes) as total_minutes
from fitbit_activities fa
         join health h on h.date = fa.date
group by week
order by week desc)
;


/************************************************** RLP - EDUCATION ***************************************************/
create or replace view rlp_daily_education as
(
select *,
    case when total_minutes >= 14.5 or total_reviews >= 145 then 1 else 0 end as complete
from (select date(date_trunc('day', created_at)) as created_at,
          (sum(time_ms) / 60000) as total_minutes,
          count(time_ms) as total_reviews
      from anki_reviews
      group by 1) reviews_per_day
order by created_at desc
    )
;

create or replace view rlp_weekly_education as
(
select date_trunc_week_sunday(created_at) as week,
    sum(total_minutes / 7) as average_minutes,
    sum(complete) as days_completed
from rlp_daily_education
group by week
order by week DESC)
;


/************************************************* RLP - PRODUCTIVITY *************************************************/
create or replace view rlp_daily_productivity as
(
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
select date(day) as day, coalesce(sum_toodledo, 0) + coalesce(sum_habitica, 0) as total_minutes
from toodledo
         full outer join habitica using (day)
    )
;

create or replace view rlp_weekly_productivity as
(
select date_trunc_week_sunday(day) as week,
    sum(case when total_minutes >= 30 then 1 else 0 end) as days_completed
from rlp_daily_productivity
group by 1
order by 1 desc)
;


/***************************************************** RLP - SLEEP ****************************************************/
create or replace view rlp_weekly_sleep as
(
select date_trunc_week_sunday(date_of_sleep) as week,
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
create or replace view rlp_daily as
(
select date(coalesce(date, created_at, day)) as day,
    case when total > 0
             then 1
         else
             case when ex.date < date(to_pst(current_timestamp))
                      then 0
                  else null end
        end as exercise,
    case when ed.complete = 1
             then 1
         when ed.total_reviews > 0
             then 0.5
         when ex.date < date(to_pst(current_timestamp))
             then coalesce(ed.complete, 0)
         else ed.complete end as education,
    case when p.total_minutes >= 30
             then 1
         when p.total_minutes > 0
             then .5
         else
             case when ex.date < date(to_pst(current_timestamp))
                      then 0
                  else null end
        end as productivity
from rlp_daily_exercise ex
         left join rlp_daily_education ed on ex.date = ed.created_at
         left join rlp_daily_productivity p on ex.date = p.day
order by day desc
    )
;

/* Recently-completed non-recurring Toodledo tasks */
select distinct title, completed_at
from toodledo_tasks
where title not in (select distinct title from toodledo_tasks where repeat != '') and
    completed_at >= '2020-01-03' and title not like 'Update Lifting Stats' and
    title not like 'Workout overrides' and title not like 'Update events in yearly candlestick calendar' and
    title not like 'Vacuum car' and title not like 'Change bedding %' and title not like 'Greylist inbox zero'
order by completed_at DESC
;

/* Anki time spend by tag */
select date_trunc('year', anki_reviews.created_at), round(sum(time_ms) / 3.6e+6, 2) as hours
from anki_notes
         JOIN anki_cards on anki_notes.id = anki_cards.note_id
         JOIN anki_reviews on anki_cards.id = anki_reviews.card_id
--where tags LIKE '%z::Languages::Spanish%'
--where tags LIKE '%General_Knowledge::The_Office%'
--where tags LIKE '%z::Computer_Science::Interview_Prep%'
--and anki_reviews.created_at BETWEEN '2019-01-01' AND '2020-01-01'
where tags LIKE '%z::Work::Stripe%'
group by 1
order by 1 desc
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
;

-- average anki review time by year
select left(date_trunc('year', created_at) || '', 4) as year,
    round(sum(time_ms) / 60000 / (case when date_trunc('year', created_at) < date_trunc('year', now())
                                           then 365.24
                                       else date_part('day', now() - date_trunc('year', now())) end)::numeric,
          1) as average_daily_minutes
from anki_reviews
group by date_trunc('year', created_at)
order by 1 desc
;

-- average exercise by year
select left(date_trunc('year', week) || '', 4) as year,
    round(avg(cardio_percent * 100)) as percentage,
    count(case when cardio_percent is not null then 1 end) as weeks_with_data
from rlp_weekly_exercise
where cardio_percent is not null
group by date_trunc('year', week)
order by 1 desc
;

-- average exercise by year, any exercise at all
select left(date_trunc('year', date) || '', 4) as year,
    round(sum(case when total > 0 then 1 else 0 end)::float / greatest(1, count(total)) *
          100) as percent_days_with_any_exercise,
    count(total) as days_with_data
from rlp_daily_exercise
group by 1
order by 1 desc;

select *
from anki_backlogs_by_day
where date >= '2019-10-21'
order by cards_in_backlog asc;


select category, round(sum(time_ms) / 3600000) as hours
from anki_review_time_per_category
where day >= '2019-10-21'
group by category
order by 2 desc;

-- total study time on zdone-generated cards
select round(sum(time_ms) / 3600000) as hours
from anki_notes an
         join anki_cards ac on an.id = ac.note_id
         join anki_reviews ar on ac.id = ar.card_id
where model_id % 100000000 = 0