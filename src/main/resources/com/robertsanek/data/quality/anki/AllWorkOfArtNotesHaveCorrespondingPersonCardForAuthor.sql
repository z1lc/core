/* People for which there is a work of art card but no corresponding Person card */
WITH people AS (SELECT REPLACE(REPLACE(REPLACE(split_part(fields, '","', 1), '<b>', ''), '</b>', ''), '"', '') as name
                FROM anki_notes
                WHERE model_id = (
                  SELECT id
                  FROM anki_models
                  WHERE name LIKE '%Person%')),
  leaders AS (SELECT REPLACE(REPLACE(REPLACE(split_part(fields, '","', 1), '<b>', ''), '</b>', ''), '"', '') as name
              FROM anki_notes
              WHERE model_id = (
                SELECT id
                FROM anki_models
                WHERE name LIKE '%Political Leader%')),
  unified_people AS (SELECT *
                     FROM people
                     UNION
                     SELECT *
                     FROM leaders),
  works_of_art AS (SELECT anki_notes.id as work_of_art_note_id,
                     REGEXP_REPLACE(
                       REPLACE(REPLACE(REPLACE(split_part(fields, '","', 4), '<b>', ''), '</b>', ''), '"',
                               ''), '\[sound:.*\]$', '') as name,
                       'Author, <i>' || REPLACE(split_part(fields, '","', 1), '"',
                                                '') || '</i> (' || split_part(fields, '","', 3) ||
                       ')' as urlTitle
                   FROM anki_notes
                   WHERE model_id = (
                     SELECT id
                     FROM anki_models
                     WHERE name LIKE '%Work of Art%') AND id NOT IN (--Research papers (multiple author)
                                                                     1468772334083, --Dunlosky: Improving Students’ Learning...
                                                                     1468772123164, --Lally: How are habits formed...
                                                                     1471559303322, --Malewicz: Pregel
                     --Author is org
                                                                     1521771433213, --ProPublica: Cutting old heads IBM
                                                                     1527732790109, --Harvard Business School: Goals Gone Wild
                                                                     1539053109431, --Discovery Channel
                     --Author is not famous enough to warrant inclusion or image cannot be found
                                                                     1536249216604, --Gavin Fitzgerald: Conor McGregor: Notorious
                                                                     1462758151675, --Hal Lindsey: The Late, Great Planet Earth
                                                                     1537166262958, --Dave Hughes: Off the Air (no image online)
                                                                     1531016039864, --Satoshi Nakamoto: Bitcoin
                                                                     1467267361322, --Murkoff/Mazel: What to Expect When You're Expecting
                                                                     1515802880801, --Edward C. Bailey	Author, Maximum RPM (2000)
                                                                     1421207466869, --Stephen Prata, C++ Primer Plus
                                                                     1479282653908, --Scott Oaks, Java Performance: The Definitive Guide
                                                                     1484430931523, --Peter Collier, A Most Incomprehensible Thing (2012)
                                                                     1521782107768, --Wildbow, Worm (2011 - 2013)
                                                                     1538514663195, --Dwaine B. Tinsley, Chester the Molester (1980 - 1993)
                                                                     1542492394303, --EPA
                                                                     1542506015816, --DMT / spirit molecule
                                                                     1542879193491, --Alfred Lansing (couldn't find pic)
                                                                     0))
SELECT 'nid:' || work_of_art_note_id /*, REPLACE(works_of_art.name, '"', '') as name, urlTitle*/
FROM works_of_art
       FULL OUTER JOIN unified_people ON works_of_art.name = unified_people.name
WHERE unified_people.name IS NULL AND works_of_art.name NOT LIKE '%<div>%' AND works_of_art.name NOT LIKE '%<br>%'
ORDER BY 1 ASC
;