SELECT DISTINCT anki_models.name
FROM anki_notes
       INNER JOIN anki_models ON anki_notes.model_id = anki_models.id
       inner join anki_templates ON anki_notes.model_id = anki_templates.model_id
WHERE (anki_notes.fields LIKE '%\\[%' OR anki_notes.fields LIKE '%\\(%' OR anki_notes.fields LIKE '%$$%') AND
  (front_html NOT LIKE '%latest.js?config=TeX-AMS_CHTML%' OR back_html NOT LIKE '%latest.js?config=TeX-AMS_CHTML%')
ORDER BY 1 ASC