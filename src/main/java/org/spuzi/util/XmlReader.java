package org.spuzi.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import org.spuzi.domain.News;

import com.saxonica.xqj.SaxonXQDataSource;

public class XmlReader implements IReader {

	static final String NEWS = "NEWS";
	static final String PIECE_NEWS = "PIECE_NEWS";
	static final String TITLE = "TITLE";
	static final String DESCRIPTION = "DESCRIPTION";
	static final String IMAGE_URL = "IMAGE_URL";
	static final String PUBLICATION_DATE = "PUBLICATION_DATE";

	@Override
	public List<News> getNews(String feedUrl) {
		String xmlFiltered = null;
		// transform the input xml to a simpler xml with only the data that we want
		try {
			xmlFiltered = filterXml(feedUrl);
		} catch (XQException e) {
			System.out.println("Error getting the news.");
			e.printStackTrace();
			return null;
		}

		// create the List of News objects from the xml
		try {
			return createNews(xmlFiltered);
		} catch (XMLStreamException e) {
			System.out.println("Error creating the news");
			e.printStackTrace();
			return null;
		}

	}

	// @formatter:off
	/**-
	 * Return another XML with the data we need in a simpler format.
	 * 
	 *	<NEWS> 
	 * 		<PIECE_NEWS> 
	 *			<TITLE>Library book returned after 52 years with cheque to cover fine</TITLE> 
	 *			<DESCRIPTION>Library book borrowed in 1967 returned with $100 cheque to cover fine...</DESCRIPTION>
	 *			<IMAGE_URL>https://nos.nl/data/image/2019/06/02/554202/1008x567.jpg</IMAGE_URL> 
	 *			<PUBLICATION_DATE>Sun, 02 Jun 2019 17:21:14+0200</PUBLICATION_DATE> 
	 *		</PIECE_NEWS> 
	 *	</NEWS>	 
	 */
	// @formatter:on
	private static String filterXml(String feedUrl) throws XQException {
		String query = "declare variable $xml := doc('" + feedUrl + "'); " + "element{'NEWS'}{ "
				+ "	for $pieceOfNews in $xml//item " + "		return element{'PIECE_NEWS'}{ "
				+ "			element{'TITLE'}{data($pieceOfNews/title)}, "
				+ "			element{'DESCRIPTION'}{data($pieceOfNews/description)}, "
				+ "			element{'IMAGE_URL'}{data($pieceOfNews/enclosure/@url)}, "
				+ "			element{'PUBLICATION_DATE'}{data($pieceOfNews/pubDate)} " + "    } " + "} ";
		// @formatter:on

		InputStream inputStream = new ByteArrayInputStream(query.getBytes());
		XQDataSource ds = new SaxonXQDataSource();
		XQConnection conn = ds.getConnection();
		XQPreparedExpression exp = conn.prepareExpression(inputStream);
		XQResultSequence result = exp.executeQuery();
		return result.next() ? result.getItemAsString(null) : null;
	}

	/**
	 * Create a list of News objects from a XML.
	 */
	private List<News> createNews(String xmlFiltered) throws XMLStreamException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		InputStream in = new ByteArrayInputStream(xmlFiltered.getBytes());
		XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

		List<News> listOfNews = new ArrayList<News>();
		News pieceOfNews = null;

		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();
			if (event.isStartElement()) {
				String localPart = event.asStartElement().getName().getLocalPart();
				switch (localPart) {
				case PIECE_NEWS:
					pieceOfNews = new News();
					break;
				case TITLE:
					pieceOfNews.setTitle(getCharacterData(event, eventReader));
					break;
				case DESCRIPTION:
					pieceOfNews.setDescription(getCharacterData(event, eventReader));
					break;
				case IMAGE_URL:
					pieceOfNews.setImageUrl(getCharacterData(event, eventReader));
					break;
				case PUBLICATION_DATE:
					// example of format input: Mon, 03 Jun 2019 00:41:53 +0200
					String date = getCharacterData(event, eventReader);
					Date publicationData = null;
					try {
						publicationData = new SimpleDateFormat( "E, dd MMM yyyy HH:mm:ss Z"  , new Locale("en", "UK")).parse(date);
					} catch (ParseException e) {
						System.out.println("Error parsing the date.");
						e.printStackTrace();
					}
					pieceOfNews.setPublicationData(publicationData);
					break;
				}
			} else if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart() == PIECE_NEWS) {
					listOfNews.add(pieceOfNews);
				}
			}
		}

		return listOfNews;
	}

	/**
	 * Get the data inside a tag.
	 * <TITLE>NRC: 1600 asielkinderen verdwenen uit opvang</TITLE> --> NRC: 1600 asielkinderen verdwenen uit opvang
	 */
	private String getCharacterData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
		String result = "";
		event = eventReader.nextEvent();
		if (event instanceof Characters) {
			result = event.asCharacters().getData();
		}
		return result;
	}

}