SELECT 'nid:' || anki_notes.id
FROM anki_notes
WHERE fields LIKE '%<br />%' OR fields like '%<br / >%' OR fields like '%<br/>%' OR fields like '%<BR>%' OR
  fields LIKE '%<BR />%' OR fields like '%<BR / >%' OR fields like '%<BR/>%'