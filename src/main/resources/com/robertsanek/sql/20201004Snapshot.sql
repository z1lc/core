-- Queries used in The Snapshot #2 / October 2020

-- Exercise / Cardio - target is 70%+ of days have at least 10 minutes of cardio
select sum(case when fairly_active_minutes + very_active_minutes > 10 then 1 else 0 end)::float /
       (6 * 7 - 1) as percent_days_with_exercise,
    avg(fairly_active_minutes + very_active_minutes) * 7 as average_weekly_lifting_sessions
from fitbit_activities
where date >= '2020-08-23' and date < '2020-10-03';

-- Exercise / Lifting - target is 30%+ of days have at least 5 minutes of lifting
select sum(case when lifting > 0 then 1 else 0 end)::float / (6 * 7 - 1) * 7 as percentage_days_lifting
from health
where date >= '2020-08-23' and date < '2020-10-03';

-- Sleep
with hm as (select date_of_sleep,
                extract(hour from start_time) + 2 as h,
                extract(minute from start_time) as m
            from fitbit_sleep)
select avg(case when h < 12 then h + 24 else h end + m / 60) - 24 as average_bedtime
from hm
where date_of_sleep >= '2020-08-23' and date_of_sleep < '2020-10-03';

-- Books
select title, author_name
from goodreads_books
where read_at >= '2020-08-23' and read_at < '2020-10-03';

-- Anki
select sum(total_minutes) / (6 * 7 - 1) as avg_minutes,
    sum(case when total_minutes >= 14.5 then 1.0 else 0.0 end) / (6 * 7 - 1) as percent
from rlp_daily_education
where created_at >= '2020-08-23' and created_at < '2020-10-03';
