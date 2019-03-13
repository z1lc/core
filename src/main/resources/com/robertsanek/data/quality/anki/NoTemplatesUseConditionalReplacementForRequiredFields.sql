/* Templates that still use conditional clauses for required fields */
SELECT DISTINCT 'note:' || anki_models.name
FROM anki_templates
       JOIN anki_models on anki_templates.model_id = anki_models.id
WHERE anki_templates.front_html LIKE '%#⭐%' OR anki_templates.back_html LIKE '%#⭐%' OR
  anki_templates.front_html LIKE '%#*%' OR anki_templates.back_html LIKE '%#*%'
;