SELECT anki_templates.id, anki_models.name as model_name, anki_templates.name as template_name
FROM anki_templates
       JOIN anki_models ON anki_templates.model_id = anki_models.id
WHERE front_html LIKE '%Pronounce %' AND front_html NOT LIKE '%class="underline"%' AND
  front_html NOT LIKE '%Pronounce <u>%' AND anki_templates.id NOT IN (
  14165050684450 --alphabets note type uses <u> in a way where it cannot be right after the word Pronounce
  )
ORDER BY model_name ASC