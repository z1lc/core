CREATE OR REPLACE FUNCTION most_recent_sunday()
    RETURNS timestamp without time zone AS
$$
BEGIN
    RETURN (select date_trunc('week', to_pst(current_timestamp) + interval '1 day') - interval '1 day');
END;
$$
    LANGUAGE plpgsql
;

CREATE OR REPLACE FUNCTION date_trunc_week_sunday(input timestamp without time zone)
    RETURNS timestamp without time zone AS
$$
BEGIN
    RETURN (select date_trunc('week', input + interval '1 day') - interval '1 day');
END;
$$
    LANGUAGE plpgsql
;

CREATE OR REPLACE FUNCTION extract_quarter(input timestamp without time zone)
    RETURNS varchar AS
$$
BEGIN
    RETURN (select extract(year from input) || 'Q' || extract(quarter from input));
END;
$$
    LANGUAGE plpgsql
;

CREATE OR REPLACE FUNCTION day_of_week_ordinal()
    RETURNS int AS
$$
BEGIN
    RETURN (select 1 + date_part('days', to_pst(current_timestamp) - most_recent_sunday()));
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
