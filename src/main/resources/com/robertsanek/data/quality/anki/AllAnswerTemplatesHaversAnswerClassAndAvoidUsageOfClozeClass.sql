WITH relevant_models AS (SELECT *
                         FROM anki_models
                         WHERE name NOT LIKE '⏸%' AND name NOT LIKE 'AnKindle%' AND name NOT LIKE '~%' AND
                           name NOT LIKE 'Command-Line' AND name NOT LIKE 'Image Occlusion Enhanced' AND name NOT LIKE 'The %'),
  relevant_templates AS (SELECT * FROM anki_templates WHERE name NOT LIKE '@D%')
SELECT DISTINCT '("note:' || relevant_models.name || '" "card:' || relevant_templates.name || '")'
FROM relevant_templates
       JOIN relevant_models ON relevant_templates.model_id = relevant_models.id
WHERE (back_html NOT LIKE '%rsAnswer%' AND back_html NOT LIKE '%rsCloze%' AND
       NOT (front_html LIKE '%{{type:%' OR front_html LIKE '%{{cloze:%')) OR
  front_html LIKE '%class="cloze"%' OR
  back_html LIKE '%class="cloze"%'
ORDER BY 1 ASC
;