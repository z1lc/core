package com.robertsanek.data.quality.anki;

import java.util.regex.Pattern;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class AllClozeTablesHaveDeletionForRowOrColumnTitle extends DataQualityBase {

  static final Log log = Logs.getLog(AllClozeTablesHaveDeletionForRowOrColumnTitle.class);
  private static final Pattern hasClozedTable = Pattern.compile("<table>[\\s\\S]*\\{\\{c");
  private static final Pattern hasThCloze = Pattern.compile("<table>[\\s\\S]*<th>.*?\\{\\{c\\d.+</th>");
  private static final Pattern hasFirstTdCloze =
      Pattern.compile("<tr>\\s*<td( class=\"tablecloze\")?>(<code>)?(<span class=\"code\">)?[A-z ]*?\\{\\{c\\d.+</td>");
  private static final ImmutableSet<Long> NOTE_ID_EXCLUSIONS = ImmutableSet.of(
      1517966960047L,
      1516295483881L,
      1515701537951L,
      1529021122258L, 1529021710896L, //paired age range for toddler/fetus etc
      1527560538018L,
      1525744139390L,
      1531688163926L,
      1531432982415L,
      1531432953115L,
      1529805649909L,
      1524687063992L,
      1539990368508L,
      1538531488483L,
      1531014520572L,
      1521429329813L,
      1541284096745L,
      1541282994391L,
      1540957117818L,
      1540911616221L,
      1540907587063L, 1540907558148L, //paired alkyl / carbon atoms
      1540270256433L,
      1540269982520L,
      1537353195250L,
      1520869166542L,
      1515472658585L,
      1515088919339L,
      1413766021643L,
      1413783293058L,
      1417928558948L,
      1418603989608L,
      1420454475147L,
      1420555116199L,
      1421937240192L,
      1422540140728L,
      1422731172430L,
      1423694071938L,
      1423774301585L,
      1424137918272L,
      1471143994456L,
      1471287969065L,
      1484425905700L,
      1494441897311L,
      1502318628967L,
      1510250185732L,
      1510334252733L,
      1511972730653L,
      1512433850788L,
      1513264386308L,
      1513379726327L,
      1515471935862L,
      1511545652331L,
      1542773786353L,
      1529220281341L,
      1543028996037L,
      1424125466923L,
      1527827098551L,
      1435024803685L,
      1511196404375L,
      1546907641919L,
      1546932897593L,
      1547093530896L,
      1548043979156L,
      1548022196496L,
      1548021176806L,
      1548310195662L,
      1548927571015L,
      1550193694261L, //tree images
      1549948376834L, //baseball consistency
      1549946944965L, //consistency total available / unavailable
      1549945588770L, //consistency abbrevs
      1549744921040L, //ML/Lisp parent/child
      1549664213407L, //performance timings
      1549662404658L, //fruits/vegetables grow...
      1549323237174L, //Python numeric/sequence
      1550631007369L,
      1550631499961L,
      1550633044944L,
      1551911896871L,
      1552174367099L,
      1534845749748L,
      1552524197755L,
      1552519832725L,
      1553555027201L,
      1553555227318L,
      1553561513280L,
      1553658128067L,
      1553649621496L,
      1554573175835L,
      1554665004519L,
      1555440788700L,
      1555694973015L,
      1556136604055L,
      1556137217855L,
      1556141242631L,
      1556220646048L,
      1556404506544L,
      1556408938486L,
      1556409826564L,
      1556402799223L,
      1556411513684L,
      1556751340169L,
      1557011482326L,
      1560013888310L,
      1560117329398L,
      1560721817943L,
      1560722769540L,
      1560721802156L,
      1560987195748L,
      1561658297023L,
      1561663038044L,
      1562002233062L,
      1562004716054L,
      1561744748779L,
      1564894601797L,
      1566537806029L,
      1566662444318L,
      1567135150069L,
      1567643361070L,
      1568173017172L,
      1568089216439L,
      1578092715175L,
      1578437699356L,
      1579558704915L,
      1579754978839L,
      1579908959928L,
      0L
  );

  @VisibleForTesting
  static boolean hasViolation(String field) {
    return hasClozedTable.matcher(field).find() && !CODE_TABLE.matcher(field).find()
        && !(hasThCloze.matcher(field).find() || hasFirstTdCloze.matcher(field).find());
  }

  @Override
  void runDQ() {
    getAllNotesInRelevantDecks(CLOZE_MODEL_ID, NOTE_ID_EXCLUSIONS).stream()
        .filter(note -> hasViolation(splitCsvIntoCommaSeparatedList(note.getFields()).get(0)))
        .forEach(note -> violations.put(this.getClass(), "nid:" + note.getId()));
  }
}
