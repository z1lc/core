/* Ensures all core cards have Add Reverse, Context, and Source fields */
SELECT 'note:' || name
FROM anki_models
WHERE (fields NOT LIKE '%"🔹Add Reverse 🔀"%' OR fields NOT LIKE '%"Context 💡"%' OR
       fields NOT LIKE '%"Source 🎯"%') AND (name ~ '^[0-9].*') AND id NOT IN (
  1512260171746 --overlapping cloze
  )
ORDER BY name ASC
;