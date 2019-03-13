/* Cards that still use the default asterisk instead of the star emoji to denote a required field */
SELECT name as model_name, fields
FROM anki_models
WHERE fields LIKE '%*%'
ORDER BY model_name ASC
;