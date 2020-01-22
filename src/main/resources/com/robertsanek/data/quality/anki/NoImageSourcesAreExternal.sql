SELECT *
FROM anki_notes
WHERE (fields LIKE '%src="http%' OR
        fields LIKE '%src=''http%')
        and model_id not in (1579060616046) --spotify song