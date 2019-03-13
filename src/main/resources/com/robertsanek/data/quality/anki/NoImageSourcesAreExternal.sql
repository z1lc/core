SELECT *
FROM anki_notes
WHERE fields LIKE '%src="http://%' OR fields LIKE '%src="https://%'