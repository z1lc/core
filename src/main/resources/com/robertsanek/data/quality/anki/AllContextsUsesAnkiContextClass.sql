select '(note:"' || anki_models.name || '" card:"' || anki_templates.name || '")'
from anki_templates
       join anki_models on anki_templates.model_id = anki_models.id
where (front_html like '%Context%' and front_html not like '%rsAnkiContext%') or
  (back_html like '%Context%' and back_html not like '%rsAnkiContext%')