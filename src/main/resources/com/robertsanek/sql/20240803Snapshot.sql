-- Queries used in The Snapshot #19 / August 2024
-- https://docs.google.com/document/d/1gLjSpSnRE9FZhUfoNiwxzXXhs_rWy3qzTje4R9hYlq4/edit#heading=h.7rcw5ud52rs

CREATE OR REPLACE FUNCTION weeks() RETURNS integer AS
$$
BEGIN
    RETURN 11;
END;
$$ LANGUAGE plpgsql;


-- Anki
select sum(total_minutes) / (weeks() * 7 - 2) as avg_minutes,
    sum(case when total_minutes >= 14.5 then 1.0 else 0.0 end) / (weeks() * 7) as percent
from rlp_daily_education
where created_at >= '2024-05-19' and created_at < '2024-08-04';
