-- Queries used in 🔒 End-of-2023 Personal Review
-- https://docs.google.com/document/d/1J3D11yc2KgIcw0BxHkCz93CUk-9X8oUhQtW9yg-6mB0/edit

CREATE OR REPLACE FUNCTION weeks() RETURNS integer AS
$$
BEGIN
    RETURN 52;
END;
$$ LANGUAGE plpgsql;


-- Anki
select sum(total_minutes) / (weeks() * 7) as avg_minutes,
    sum(case when total_minutes >= 14.5 then 1.0 else 0.0 end) / (weeks() * 7) as percent
from rlp_daily_education
where created_at >= '2023-01-01' and created_at < '2024-12-31';
