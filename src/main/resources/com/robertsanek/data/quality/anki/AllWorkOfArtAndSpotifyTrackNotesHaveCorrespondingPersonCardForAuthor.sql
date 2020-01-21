/* People for which there is a work of art card but no corresponding Person card */
WITH people AS (SELECT split_part(
  REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(split_part(fields, '","', 1),
                                          '<b>', ''),
                                  '</b>', ''),
                          '"', ''),
                  '<u>', ''),
          '</u>', ''),
  ' or', 1) as name
                FROM anki_notes
                WHERE model_id = (
                  SELECT id
                  FROM anki_models
                  WHERE name LIKE '%Person%')),
    numbers as (SELECT * FROM GENERATE_SERIES(1, 5)),
     tracks as (SELECT anki_notes.id as work_of_art_note_id,
                    REGEXP_REPLACE(
                      REPLACE(REPLACE(REPLACE(split_part(fields, '","', 3), '<b>', ''), '</b>', ''), '"',
                              ''), '\[sound:.*\]$', '') as name
                FROM anki_notes
                WHERE model_id = (
                    SELECT id
                    FROM anki_models
                    WHERE name LIKE 'Spotify Track')),
     songs as (
               select min(work_of_art_note_id) as work_of_art_note_id, split_part(name, ', ', generate_series) as name
               from tracks
                        join numbers on true
               group by 2),
  works_of_art AS (SELECT anki_notes.id as work_of_art_note_id,
                     REGEXP_REPLACE(
                       REPLACE(REPLACE(REPLACE(split_part(fields, '","', 4), '<b>', ''), '</b>', ''), '"',
                               ''), '\[sound:.*\]$', '') as name
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
                                                                     1542879193543, --Mittie Hellmich (couldn't find pic)
                                                                     1539753868266,
                                                                     1421205009429,
                                                                     1421207265551,
                                                                     1421207514429,
                                                                     1484430821746,
                                                                     1484432378503,
                                                                     1542879193520,
                                                                     1542879193522,
                                                                     0)),
     both_types as (select * from songs UNION (select * from works_of_art))
SELECT 'nid:' || work_of_art_note_id /*, REPLACE(works_of_art.name, '"', '') as name, urlTitle*/
FROM both_types
       FULL OUTER JOIN people ON both_types.name = people.name
WHERE people.name IS NULL AND both_types.name NOT LIKE '%<div>%' AND both_types.name NOT LIKE '%<br>%'
ORDER BY 1 ASC
;