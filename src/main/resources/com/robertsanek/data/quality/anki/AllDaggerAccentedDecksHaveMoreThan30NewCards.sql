with autoadd_backfill_decks as (select name, id
                                from anki_decks
                                where name like '%†'),
  new_per_deck as (select name, count(*) as new_cards
                   from anki_cards
                          join autoadd_backfill_decks on anki_cards.deck_id = autoadd_backfill_decks.id
                   where deck_id in (select id from autoadd_backfill_decks) and queue = 'UNSEEN'
                   group by name
                   order by 1 asc)
select 'deck:"' || name || '"'
from new_per_deck
where new_cards <= 30
;