-- Queries used in The Snapshot #15 / October 2023
-- https://docs.google.com/document/d/1Y8uqNeyXJKGh8HVBX7cQtMJf0ye0HGr9RTU2Uy1cM9E

CREATE OR REPLACE FUNCTION weeks() RETURNS integer AS $$
BEGIN
    RETURN 10;
END;
$$ LANGUAGE plpgsql;


-- Anki
select sum(total_minutes) / (weeks() * 7)                                         as avg_minutes,
       sum(case when total_minutes >= 14.5 then 1.0 else 0.0 end) / (weeks() * 7) as percent
from rlp_daily_education
where created_at >= '2023-08-06'
  and created_at < '2023-10-15';
