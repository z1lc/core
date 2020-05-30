/* Ensures all cards have the text part before the image apart, both on front and on back sides. */
WITH relevant_models AS (SELECT *
                         FROM anki_models
                         WHERE name NOT LIKE '⏸%' AND name NOT LIKE 'AnKindle%' AND name NOT LIKE '~%' AND
                             name NOT LIKE 'Command-Line' AND name NOT LIKE 'Image Occlusion Enhanced' AND name NOT LIKE 'The %'),
    cards as (SELECT DISTINCT '("note:' || relevant_models.name || '" "card:' || anki_templates.name || '")' as search_term
FROM anki_templates
       JOIN relevant_models on anki_templates.model_id = relevant_models.id
WHERE front_html LIKE '%imagePart%textPart%' OR
  back_html LIKE '%imagePart%textPart%' OR
  front_html not like '%textPart%' OR
  (back_html not like '%textPart%' AND back_html not like '%{{FrontSide}}%')
ORDER BY 1)
SELECT * FROM cards
WHERE search_term NOT LIKE '%Extra%Template%'
;