-- Queries used in The Snapshot #5 / March 2021
-- https://docs.google.com/document/d/1-x8dtw5qWq7cDSMnkAo3Aiw4J4YjbbEvuMwt0zBN5Pc/edit#heading=h.g186q4tilmaq

-- Exercise / Cardio - target is 70%+ of days have at least 10 minutes of cardio
select sum(case when active_zone_minutes > 10 then 1 else 0 end)::float /
       (11 * 7)                     as percent_days_with_exercise,
       avg(active_zone_minutes) * 7 as average_weekly_active_zone_minutes
from fitbit_activities
where date >= '2021-03-21'
  and date < '2021-06-06';

-- Sleep
-- Validate by spot-checking vs. fitbit.com -- they seem to change the DateTime's here kind of frequently!
with hm as (select date_of_sleep,
                   extract(hour from start_time)   as h,
                   extract(minute from start_time) as m,
                   time_in_bed
            from fitbit_sleep)
select avg(case when h < 12 then h + 24 else h end + m / 60) - 24 as average_bedtime,
       avg(time_in_bed / 60.0)                                    as hours_in_bed
from hm
where date_of_sleep >= '2021-03-21'
  and date_of_sleep < '2021-06-06';

-- Books
select title, author_name
from goodreads_books
where read_at >= '2021-03-21'
  and read_at < '2021-06-06';

-- Anki
select sum(total_minutes) / (11 * 7)                                         as avg_minutes,
       sum(case when total_minutes >= 14.5 then 1.0 else 0.0 end) / (11 * 7) as percent
from rlp_daily_education
where created_at >= '2021-03-21'
  and created_at < '2021-06-06';
