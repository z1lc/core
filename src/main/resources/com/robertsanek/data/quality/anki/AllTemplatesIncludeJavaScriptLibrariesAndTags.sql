/* Ensures all templates include jQuery, _AnkiLibrary.js, the tag/category identifier div, and auto-call _AnkiLibrary.js */
SELECT anki_models.id as model_id,
  anki_models.name as model_name,
  anki_templates.name as template_name
FROM anki_templates
       JOIN anki_models on anki_templates.model_id = anki_models.id
WHERE (front_html NOT LIKE ('%<script type="text/javascript" src="_jquery-1.11.2.min.js"></script>' || E'\n'
  || '<div id="categoryIdentifierFront">{{Tags}}</div>' || E'\n'
  || '<script type="text/javascript" src="_AnkiLibrary.js"></script>' || E'\n'
  ||
                            '<script type="text/javascript">if (typeof rsAnki !== ''undefined'') rsAnki.defaultUnified();</script>%')
  OR back_html NOT LIKE ('%<script type="text/javascript" src="_jquery-1.11.2.min.js"></script>' || E'\n'
    || '<div id="categoryIdentifierBack">{{Tags}}</div>' || E'\n'
    || '<script type="text/javascript" src="_AnkiLibrary.js"></script>' || E'\n'
    ||
                         '<script type="text/javascript">if (typeof rsAnki !== ''undefined'') rsAnki.defaultUnified();</script>%')) AND
  anki_models.name NOT LIKE '⏸️%' --Notes with a pause symbol as the first character mean they are currently unused
  AND anki_models.name NOT LIKE 'AnKindle%' --AnKindle Add-On card type
  AND anki_models.name NOT LIKE 'Cloze (overlapping)' --Cloze Overlapper Add-On card type
  AND model_id NOT IN (1342697730642, --original basic note
                       1399688174448, --original cloze note
                       1487029322167, --Will's Cloze
                       1511625126772, --Will's Basic
                       1541366068534, --IR card
                       0)
ORDER BY model_name ASC
;