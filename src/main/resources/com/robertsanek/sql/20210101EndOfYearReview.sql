-- Sleep
-- Validate by spot-checking vs. fitbit.com -- they seem to change the DateTime's here kind of frequently!
with hm as (select date_of_sleep,
                extract(hour from start_time) as h,
                extract(minute from start_time) as m,
                time_in_bed
            from fitbit_sleep
            where is_main_sleep)
select date_trunc('month', date_of_sleep),
    avg(case when h < 12 then h + 24 else h end + m / 60) - 24 as average_bedtime,
    avg(time_in_bed / 60.0) as hours_in_bed
from hm
-- comment out below line and use extract_quarter for full data view
where date_of_sleep >= '2020-01-01' and date_of_sleep < '2021-01-01'
group by 1
order by 1 asc;

select date_trunc('month', date),
    avg(resting_heart_rate) as avg_heart_rate,
    avg(fairly_active_minutes + fitbit_activities.very_active_minutes) as avg_active_minutes
from fitbit_activities
-- comment out below line and use extract_quarter for full data view
where date >= '2020-01-01' and date < '2021-01-01'
group by 1
order by 1 asc

-- Books (this year, for some reason excludes Grapes of Wrath)
select title, author_name, read_at
from goodreads_books
where read_at >= '2020-01-01' and read_at < '2021-01-01'
order by read_at asc;

-- Books read by year
select extract(year from read_at), count(*)
from goodreads_books
where read_at is not null
group by 1
order by 1 asc;

-- RescueTime Video / Games
select extract(year from date), sum(time_spent_seconds) / 60 / count(distinct date) as hours_video_and_games
from rescuetime_daily_categories
where (category = 'Video' or
       category = 'Games')
    --and date >= '2020-01-01'
group by 1
order by 1 asc;

-- before vs after (2021), media consumption of games vs. video
with games as (select * from rescuetime_daily_categories where category = 'Games'),
    video as (select * from rescuetime_daily_categories where category = 'Video'),
    all_days as (select day from rlp_daily)
select case when day > '2021-01-01' then '2021' else '2019 & 2020' end as time_period,
    round(sum(games.time_spent_seconds) / 60 / count(distinct day)) as minutes_games,
    round(sum(video.time_spent_seconds) / 60 / count(distinct day)) as minutes_video,
        (sum(video.time_spent_seconds) + sum(games.time_spent_seconds)) / 60 / count(distinct day) as minutes_total,
    round(sum(games.time_spent_seconds)::float /
          (sum(video.time_spent_seconds) + sum(games.time_spent_seconds)) * 100) as percent_games,
    count(distinct day)
from all_days
         full outer join games on date_trunc('day', date) = day
         full outer join video on date_trunc('day', video.date) = day
where day >= '2019-10-21'
group by 1
order by 1 asc;