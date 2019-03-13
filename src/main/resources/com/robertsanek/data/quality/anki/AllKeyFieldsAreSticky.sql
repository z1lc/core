SELECT DISTINCT 'note:' || anki_models.name
FROM anki_fields
       INNER JOIN anki_models ON anki_fields.model_id = anki_models.id
WHERE sticky IS NOT TRUE AND (anki_fields.name = 'Source 🎯'
  OR anki_fields.name = 'Context 💡'
  OR anki_fields.name = 'Add Reverse 🔀')
ORDER BY 1 ASC