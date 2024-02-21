import random
from datetime import date, timedelta

from anki.utils import intTime
from aqt import mw
from aqt.qt import *

# card.queue: 0=new, -1=suspended

# what is the maximum amount of new cards you should see per day for an individual table?
MAXIMUM_PER_DAY = 15

# what is the maximum amount of days until you review all cells in a table?
# this setting will be overriden by MAXIMUM_PER_DAY
AMORTIZE_OVER = 7

DECKS = ['z::0 ClozeTableManager']


def auto_manage():
    for deck in DECKS:
        ctc_subdeck = mw.col.decks.byName(deck)
        ctc_subdeck_note_ids = set(mw.col.db.list("""select notes.id
            from notes
            join cards on notes.id = cards.nid
            where did = {}""".format(ctc_subdeck['id'])))

        for note_id in ctc_subdeck_note_ids:
            this_table_card_ids = set(mw.col.db.list("""select id
                from cards
                where nid = {}""".format(note_id)))
            this_table_card_ids_new = set(mw.col.db.list("""select id
                from cards
                where nid = {} 
                and queue in ({})""".format(note_id, "0, -1")))
            last_day_of_review = date.fromtimestamp(note_id / 1000) + timedelta(days=AMORTIZE_OVER)
            days_left = max((last_day_of_review - date.today()).days, 1)
            per_day = max(int(round(len(this_table_card_ids) / days_left)), 1)
            final_per_day = min(per_day, len(this_table_card_ids_new), MAXIMUM_PER_DAY)
            to_unsuspend = random.sample(this_table_card_ids_new, final_per_day)
            for card_id in this_table_card_ids_new:
                current_queue = mw.col.db.scalar("select queue from cards where id = {}".format(card_id))
                # if a card is currently brand-new or suspended (ignore cards in learning and under review)
                if current_queue in [-1, 0]:
                    queue_column_setting = -1
                    if card_id in to_unsuspend:
                        queue_column_setting = 0
                    mw.col.db.execute("""update cards set mod=?, usn=?, queue=? where id = ?""",
                                      intTime(), mw.col.usn(), queue_column_setting, card_id)

    # reset main window so that due counts per subdeck update
    mw.reset()


trigger_action = QAction("ClozeTableManger: Trigger automanage", mw)
trigger_action.triggered.connect(auto_manage)
mw.form.menuTools.addAction(trigger_action)
mw.addonManager.setConfigAction(__name__, auto_manage)
