SELECT *
FROM anki_decks
WHERE name LIKE '%[autoadd]%' AND option_group_id NOT IN (487065549, 1787323124)