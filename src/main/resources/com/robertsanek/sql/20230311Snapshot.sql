-- Queries used in The Snapshot #12 / March 2023
-- https://docs.google.com/document/d/1-x8dtw5qWq7cDSMnkAo3Aiw4J4YjbbEvuMwt0zBN5Pc/edit#

-- Anki
select sum(total_minutes) / (11 * 7)                                         as avg_minutes,
       sum(case when total_minutes >= 14.5 then 1.0 else 0.0 end) / (14 * 7) as percent
from rlp_daily_education
where created_at >= '2023-01-01'
  and created_at < '2023-03-12';
