import json

from anki.hooks import wrap
from aqt.addcards import AddCards


def add_cards_pre(self, _old):
    self.editor.saveNow(lambda: add_cards_wrapper(self, _old))


def add_cards_wrapper(self, _old):
    cols = []
    model = self.editor.note.model()
    empty_required_fields = []
    for field_index, field in enumerate(self.editor.note.fields):
        name = model['flds'][field_index]['name']
        if '⭐' in name and field == '':
            cols.append("#fcc")
            empty_required_fields.append(name)
        else:
            cols.append("#fff")
    if empty_required_fields:
        self.editor.web.eval("setBackgrounds(%s);" % json.dumps(cols))
        return
    else:
        return _old(self)


AddCards.addCards = wrap(AddCards.addCards, add_cards_pre, 'around')
