select 'nid:' || id
from anki_notes
where fields like '%<br>",%' and id not in (1422756472533)
;
