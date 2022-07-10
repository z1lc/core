-- Queries used in The Snapshot #10 / July 2022
-- https://docs.google.com/document/d/1-x8dtw5qWq7cDSMnkAo3Aiw4J4YjbbEvuMwt0zBN5Pc/edit#
-- connect via `gcloud sql connect rsanek-db --user=postgres --quiet`
-- password is in secrets.json

-- Exercise / Cardio - target is 70%+ of days have at least 10 minutes of cardio
select sum(case when active_zone_minutes > 10 then 1 else 0 end)::float /
       (14 * 7)                     as percent_days_with_exercise,
       avg(active_zone_minutes) * 7 as average_weekly_active_zone_minutes
from fitbit_activities
where date >= '2022-04-03'
  and date < '2022-07-10';

-- Sleep
-- Validate by spot-checking vs. fitbit.com -- they seem to change the DateTime's here kind of frequently!
with hm as (select date_of_sleep,
                   extract(hour from start_time)   as h,
                   extract(minute from start_time) as m,
                   time_in_bed
            from fitbit_sleep),
     summarized as (
select (case when h < 12 then h + 24 else h end + m / 60) - 24 as bedtime,
       time_in_bed / 60.0                                    as hours_in_bed
from hm
where date_of_sleep >= '2022-04-03'
  and date_of_sleep < '2022-07-10')
select percentile_cont(0.5) within group (order by bedtime) as median_bedtime,
      percentile_cont(0.5) within group (order by hours_in_bed) as median_hours_in_bed
from summarized;

-- Books
select title, author_name
from goodreads_books
where read_at >= '2022-04-03'
  and read_at < '2022-07-10';

-- Anki
select sum(total_minutes) / (14 * 7)                                         as avg_minutes,
       sum(case when total_minutes >= 14.5 then 1.0 else 0.0 end) / (14 * 7) as percent
from rlp_daily_education
where created_at >= '2022-04-03'
  and created_at < '2022-07-10';
