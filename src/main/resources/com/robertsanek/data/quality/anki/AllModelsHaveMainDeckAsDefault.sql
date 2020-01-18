-- Change in db: col>models; search for did": 1, (1 is Default)
SELECT DISTINCT 'note:' || anki_models.name
FROM anki_models
         INNER JOIN anki_decks ON anki_models.deck_id = anki_decks.id
WHERE anki_decks.name NOT LIKE 'z%' AND anki_models.name NOT LIKE 'IR3+priority' AND
    anki_models.name NOT LIKE 'Spotify Track'
ORDER BY 1 ASC