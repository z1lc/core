SELECT *
FROM anki_notes
WHERE (fields LIKE '%src="http%' OR
       fields LIKE '%src=''http%') and
        model_id not in (
                         1587000000000, -- Spotify Track
                         1586000000000, -- Spotify Artist
                         1588000000000, -- Video
                         1589000000000, -- Video Person
                         1604800000000, -- Readwise Highlight
                         1607000000000, -- Beer
                         0
        )
