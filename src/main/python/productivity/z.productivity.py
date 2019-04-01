import os
import sys

from PyQt5.QtCore import *
from PyQt5.QtGui import *
from PyQt5.QtWebEngineWidgets import *
from PyQt5.QtWidgets import *
import platform

DEFAULT_ZOOM = 1.25
REMOVE_TOODLEDO_HEADER_JS = "var elem = document.getElementById('topnav-in'); elem.parentNode.removeChild(elem);"
REMOVE_WORKFLOWY_PADDING_JS = """
var style = document.createElement('style');
style.type = 'text/css';
style.innerHTML = 'div.page.active { padding: 0; margin: 18px auto; }';
document.getElementsByTagName('head')[0].appendChild(style);
"""


# https://stackoverflow.com/a/47736565
class WebEnginePage(QWebEnginePage):
    def acceptNavigationRequest(self, url, _type, is_main_frame):
        if _type == QWebEnginePage.NavigationTypeLinkClicked:
            QDesktopServices.openUrl(url)
            return False
        return True


class HtmlView(QWebEngineView):
    def __init__(self, custom_javascript, *args, **kwargs):
        QWebEngineView.__init__(self, *args, **kwargs)
        self.setPage(WebEnginePage(self))
        self.loadFinished.connect(self.on_load_finished)
        self.custom_javascript = custom_javascript

    def on_load_finished(self, ok):
        if ok:
            self.page().runJavaScript(self.custom_javascript)


def __get_size_policy_horizontal(horizontal_stretch):
    sp = QSizePolicy(QSizePolicy.Preferred, QSizePolicy.Preferred)
    sp.setHorizontalStretch(horizontal_stretch)
    return sp


def __get_size_policy_vertical(vertical_stretch):
    sp = QSizePolicy(QSizePolicy.Preferred, QSizePolicy.Preferred)
    sp.setVerticalStretch(vertical_stretch)
    return sp


def __get_browser(url, qt_size_policy, custom_javascript, open_links_in_browser=True):
    zoom = DEFAULT_ZOOM
    if "laptop" in platform.node().lower():
        zoom = 1.75
    browser = HtmlView(custom_javascript=custom_javascript) if open_links_in_browser else QWebEngineView()
    browser.setUrl(QUrl(url))
    browser.setZoomFactor(zoom)
    browser.setSizePolicy(qt_size_policy)
    return browser


# https://www.tutorialspoint.com/pyqt/pyqt_qboxlayout_class.htm
# https://www.tutorialspoint.com/pyqt/pyqt_qsplitter_widget.htm
# noinspection PyArgumentList
def window():
    app = QApplication(sys.argv)
    win = QWidget()

    horizontal_splitter = QSplitter(Qt.Horizontal)
    browser1 = __get_browser("https://habits.toodledo.com/", __get_size_policy_horizontal(9), REMOVE_TOODLEDO_HEADER_JS)
    horizontal_splitter.addWidget(browser1)

    vertical_splitter = QSplitter(Qt.Vertical)
    vertical_splitter.setSizePolicy(__get_size_policy_horizontal(20))
    horizontal_splitter.addWidget(vertical_splitter)

    browser2 = __get_browser("https://toodledo.com/", __get_size_policy_vertical(30), REMOVE_TOODLEDO_HEADER_JS)
    browser3 = __get_browser("https://workflowy.com/#/eacfb1db8eb3", __get_size_policy_vertical(20),
                             REMOVE_WORKFLOWY_PADDING_JS)
    vertical_splitter.addWidget(browser2)
    vertical_splitter.addWidget(browser3)

    layout = QVBoxLayout()
    layout.addWidget(horizontal_splitter)
    layout.setSpacing(0)
    layout.setContentsMargins(0, 0, 0, 0)

    win.setLayout(layout)
    win.show()
    win.setWindowTitle("z.productivity")
    win.setWindowIcon(QIcon(os.path.join('checked.png')))
    win.resize(1920, 1080)

    def reload():
        browser1.reload()
        browser2.reload()
        browser3.reload()

    shortcut = QShortcut(QKeySequence("Ctrl+R"), horizontal_splitter)
    shortcut.activated.connect(reload)

    sys.exit(app.exec_())


if __name__ == '__main__':
    window()
