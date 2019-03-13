SELECT DISTINCT 'note:' || anki_models.name || ' ' || 'card:' || anki_templates.name
FROM anki_templates
       JOIN anki_models ON anki_templates.model_id = anki_models.id
WHERE anki_templates.name LIKE 'Card%' AND
  NOT (anki_models.name LIKE '⏸%' or anki_models.name like '\_%' or anki_models.name like 'The %')