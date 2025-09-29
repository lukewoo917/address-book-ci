package com.example.addressbook;

import com.example.addressbook.model.Contact;
import com.example.addressbook.model.ContactManager;
import com.example.addressbook.model.SqliteContactDAO;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContactManagerTest {


    private ContactManager contactManager;
    private static final String EMPTY_STRING = "";
    private static final String NULL_STRING = null;
    private static final Contact[] contacts = {
            new Contact("John", "Smith", "john.smith@abc.com", "4454844847" ),
            new Contact("Albert", "Wiseman", "wise@wiseworks.com", "67832682"),
            new Contact("Amy", "Lutz", "amy@cabthree.com", "2232324"),
            new Contact("Jane Anne", "Pauls", "ja@abc.com", "7824581"),
            new Contact("Timothy", "Ward", "amy@cabthree.com", "2232324"),
            new Contact("Lucile", "Ward", "ssds@ppoiu.com", "23435232324"),
            new Contact("Amy", "Lutz", "cindy@cindy.com", "875454545455"),
            new Contact("Amyally", "Gdfa", "cindy@cabthree.com", "875454545455"),
            new Contact("Jane", "Smith", "janesmith@abc.com", "875454545455")
    };

    private static final String MATCHING_FIRSTNAME_AMY = "Amy";
    List<Contact> matches;

    @BeforeAll
    public void setup() {
        SqliteContactDAO connection = new SqliteContactDAO();
        connection.dropTable();
        contactManager = new ContactManager();
        matches = new ArrayList<>();
        for (Contact contact : contacts) {
            contactManager.addContact(contact);
        }
    }


    @AfterEach
    public void tearDown() {
        matches.clear();
    }

    @Test
    public void testSearchByFirstnameCorrectNumberNormalCase() {
        matches = contactManager.searchContacts(contacts[0].getFirstName());
        assertEquals(1, matches.size());
    }

    @Test
    public void testSearchByFirstnameCorrectNumberLowerCase() {
        matches = contactManager.searchContacts(contacts[0].getFirstName());
        assertEquals(1, matches.size());
    }

    @Test
    public void testSearchByFirstnameCorrectNumberUpperCase() {
        matches = contactManager.searchContacts(contacts[0].getFirstName());
        assertEquals(1, matches.size());
    }

    @Test
    public void testReturnAllContactsWithEmptySearchQuery() {
        matches = contactManager.searchContacts(EMPTY_STRING);
        assertEquals(contacts.length, matches.size());
    }

    @Test
    public void testReturnAllContactsWithNullSearchQuery() {
        matches = contactManager.searchContacts(NULL_STRING);
        assertEquals(contacts.length, matches.size());
    }


    @Test
    public void testSearchByLastnameCorrectNumber() {
        matches = contactManager.searchContacts(contacts[0].getLastName());
        assertEquals(2, matches.size());
    }

    @Test
    public void testSearchByPhoneCorrectNumber() {
        matches = contactManager.searchContacts(contacts[0].getPhone());
        assertEquals(1, matches.size());
    }

    @Test
    public void testSearchByEmailCorrectNumber() {
        matches = contactManager.searchContacts(contacts[0].getEmail());
        assertEquals(1, matches.size());
    }

    @Test
    public void testSearchByFirstnameMultipleMatchingContacts() {
        matches = contactManager.searchContacts(MATCHING_FIRSTNAME_AMY);
        assertEquals(3, matches.size());
    }

    @Test
    public void testSearchNoResults() {
        matches = contactManager.searchContacts("Dan");
        assertEquals(0, matches.size());
    }



    @Test
    public void testNumberSearchCaseInsensitive() {
        matches = contactManager.searchContacts("jane");
        assertEquals(2, matches.size());
    }

    @Test
    public void testSearchCaseInsensitive() {
        matches = contactManager.searchContacts("Jane");
        for (Contact contact : matches) {
            assertTrue(contact.getFirstName().contains("Jane"));
        }
    }

    @Test
    public void testSearchPartialQuery() {

        matches = contactManager.searchContacts("Amy");
        assertEquals(3, matches.size());
        assertTrue(matches.get(0).getFirstName().equals("Amy"));
        assertTrue(matches.get(1).getFirstName().equals("Amy"));
        assertTrue(matches.get(2).getFirstName().equals("Amyally"));
    }

    @Test
    public void testSearchEmptyContacts() {
        List<Contact> contacts = contactManager.searchContacts("");
        assertEquals(contacts.size(), contacts.size());
    }

    @Test
    public void testSearchByEmail() {
        List<Contact> contacts = contactManager.searchContacts(".com");
        assertEquals(9, contacts.size());
    }

    @Test
    public void testSearchByEmailSameSection() {
        List<Contact> contacts = contactManager.searchContacts("@cabthree");
        assertEquals(3, contacts.size());
    }

    @Test
    public void testSearchByPhone() {

        List<Contact> contacts = contactManager.searchContacts("4454844847");
        assertEquals(1, contacts.size());
        assertEquals("John", contacts.get(0).getFirstName());
    }

    @Test
    public void testSearchByFullName() {

        List<Contact> contacts = contactManager.searchContacts("Albert Wiseman");
        assertEquals(1, contacts.size());

    }

}
