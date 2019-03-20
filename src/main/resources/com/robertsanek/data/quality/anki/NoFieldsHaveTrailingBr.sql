select 'nid:' || id
from anki_notes
where fields like '%<br>",%'
;
