SELECT *
FROM anki_notes
WHERE (fields LIKE '%src="http%' OR
       fields LIKE '%src=''http%') and
        model_id not in (
                         1587000000000,  --spotify song
                         1586000000000,  --spotify artist
                         0
        )
