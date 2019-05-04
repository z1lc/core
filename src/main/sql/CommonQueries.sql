/***************************************************** RLP - ANKI *****************************************************/
SELECT DATE_TRUNC('week', created_at + interval '1 day') - interval '1 day' as week, SUM(total_minutes / 7),
    SUM(complete)
FROM (SELECT *, CASE WHEN total_minutes >= 14.5 OR total_reviews >= 145 THEN 1 ELSE 0 END as complete
      FROM (SELECT DATE_TRUNC('day', created_at) as created_at,
                (SUM(time_ms) / 60000) as total_minutes,
                COUNT(time_ms) as total_reviews
            FROM anki_reviews
            GROUP BY 1) reviews_per_day) b
GROUP BY week
ORDER BY week DESC
;


/************************************************* RLP - PRODUCTIVITY *************************************************/
with value_overrides as (select 'Toodledo Tasks' as title, 10 as override
                         union
                         select 'M+', 0.1
                         union
                         select 'N+', 0.1),
    summarized as (select date, toodledo_habits.title, added, value * coalesce(override, 1) as contribution,
                       coalesce(override, 1) as outof
                   from toodledo_habit_repetitions
                            join toodledo_habits on toodledo_habit_repetitions.habit = toodledo_habits.id
                            full outer join value_overrides
                   on toodledo_habits.title like '%' || value_overrides.title || '%'
                   where toodledo_habits.title like '%⭐%')
select date, sum(contribution) as contrib, sum(outof) as outof
from summarized
group by date
order by date desc
;


/* Completed Toodledo non-recurring tasks in last week */
SELECT distinct title, completed_at
FROM toodledo_tasks
WHERE title NOT IN (SELECT distinct title FROM toodledo_tasks WHERE repeat != '') AND
        completed_at >= current_date - 14 AND title NOT LIKE 'Update Lifting Stats' AND
        title NOT LIKE 'Workout overrides' AND
        title NOT LIKE 'Update events in yearly candlestick calendar' AND title NOT LIKE 'Vacuum car'
ORDER BY completed_at DESC


/* Anki time spend by tag */
SELECT ROUND(SUM(time_ms) / 3.6e+6, 2) as hours
FROM anki_notes
         JOIN anki_cards ON anki_notes.id = anki_cards.note_id
         JOIN anki_reviews ON anki_cards.id = anki_reviews.card_id
--WHERE tags LIKE '%z::Languages::Spanish%'
--WHERE tags LIKE '%General_Knowledge::The_Office%'
WHERE tags LIKE '%z::Computer_Science::Interview_Prep%'
;


/* ETL runs ordered by descending time */
SELECT *,
    ROUND(CAST(EXTRACT(epoch from end_time - start_time) as numeric)) as seconds
FROM etl_runs
ORDER BY seconds DESC
;


/****************************************************** FUNCTIONS *****************************************************/
CREATE OR REPLACE FUNCTION most_recent_sunday()
    RETURNS timestamp without time zone AS
$$
BEGIN
    RETURN (select date_trunc('week', to_pst(current_timestamp) + interval '1 day') - interval '1 day');
END;
$$
    LANGUAGE plpgsql
;
CREATE OR REPLACE FUNCTION to_pst(input timestamp without time zone)
    RETURNS timestamp without time zone AS
$$
BEGIN
    RETURN input at time zone 'utc' at time zone 'pst';
END;
$$
    LANGUAGE plpgsql
;
CREATE OR REPLACE FUNCTION to_pst(input timestamp with time zone)
    RETURNS timestamp without time zone AS
$$
BEGIN
    RETURN input at time zone 'pst';
END;
$$
    LANGUAGE plpgsql
;