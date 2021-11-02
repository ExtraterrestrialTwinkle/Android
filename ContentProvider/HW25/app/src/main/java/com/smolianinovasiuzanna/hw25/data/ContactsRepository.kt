package com.smolianinovasiuzanna.hw25.data

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.regex.Pattern
import kotlin.random.Random

class ContactsRepository(private val context: Context) {

    //private val phonePattern = Pattern.compile("^\\+?[0-9]{3}?[0-9]{6,12}\$")

    private val contactList = mutableListOf<Contact>()

    suspend fun getAllContacts(): List<Contact> {
        Log.d("ContactsRepository", "getAllContacts")
        return withContext(Dispatchers.IO) {
            context.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"

            )?.use {
                getContactsFromCursor(it)
            }
        }.orEmpty()
    }

    private fun getContactsFromCursor(cursor: Cursor): List<Contact> {
        Log.d("ContactsRepository", "getContactsFromCursor")
        if (cursor.moveToFirst().not()) return emptyList()

        do {
            val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val name = cursor.getString(nameIndex)

            val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val id = cursor.getLong(idIndex)

            contactList.add(
                Contact(
                    id = id,
                    name = name,
                    phoneNumbers = getPhoneNumbersForContact(id),
                    emails = getEmailsForContact(id)
                )
            )
        } while (cursor.moveToNext())
        Log.d("ContactList", contactList.size.toString())
        return contactList
    }

    private fun getPhoneNumbersForContact(contactId: Long): List<String> {
        Log.d("ContactsRepository", "getPhoneNumbersForContact")
        return context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )?.use {
            getPhonesFromCursor(it)
        }.orEmpty()
    }

    private fun getPhonesFromCursor(cursor: Cursor): List<String> {
        Log.d("ContactsRepository", "getPhonesFromCursor")
        if (cursor.moveToFirst().not()) return emptyList()

        val phonesList = mutableListOf<String>()

        do {
            val phoneNumberIndex = cursor.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )
            val phoneNumber = cursor.getString(phoneNumberIndex)

            phonesList.add(phoneNumber)
        } while (cursor.moveToNext())

        return phonesList
    }

    private fun getEmailsForContact(contactId: Long): List<String> {
        Log.d("ContactsRepository", "getEmailsForContact")
        return context.contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )?.use {
            getEmailsFromCursor(it)
        }.orEmpty()
    }

    private fun getEmailsFromCursor(cursor: Cursor): List<String> {
        Log.d("ContactsRepository", "getEmailsFromCursor")
        if (cursor.moveToFirst().not()) return emptyList()

        val emailsList = mutableListOf<String>()

        do {
            val emailAddressIndex = cursor.getColumnIndex(
                ContactsContract.CommonDataKinds.Email.ADDRESS
            )
            val emailAddress = cursor.getString(emailAddressIndex)

            emailsList.add(emailAddress)
        } while (cursor.moveToNext())
        return emailsList
    }

    fun getDetailContactInfo(contactId: Long): Contact {
        return contactList.find { contact ->
            contact.id == contactId
        } ?: error("No contact")
    }

    suspend fun deleteContact(contactId: Long):List<Contact>{
        withContext(Dispatchers.IO) {
            val contactUri = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI,
                contactId
            )
            context.contentResolver.delete(contactUri, null, null)
            val deletedContact = contactList.find{
                it.id == contactId
            }
            contactList.remove(deletedContact)
        }
        return contactList
    }

    suspend fun addContact(name: String, phones: List<String>, emails: List<String>): List<Contact>{

        val contactId = Random.nextLong()
        val newContact = Contact(contactId, name, phones, emails)
        contactList.add(newContact)
        withContext(Dispatchers.IO){
//            for (i in phones.indices){
//                if(phonePattern.matcher(phones[i]).matches()){
//                    throw IncorrectFormException("Некорректно введен номер телефона ${i+1}")
//                }
//            }
            saveRawContact()
            saveContactName(contactId, name)
            for (i in phones.indices) {
                saveContactPhone(contactId, phones[i])
            }
            if (emails.isNotEmpty()){
                for(i in emails.indices){
                    saveContactEmail(contactId, emails[i])
                }
            }
        }
        return contactList
    }

    private fun saveRawContact(): Long{
        val uri = context.contentResolver.insert(
            ContactsContract.RawContacts.CONTENT_URI,
            ContentValues()
        )
        Log.d("saveRawContact", "uri = $uri")
        return uri?.lastPathSegment?.toLongOrNull()?: error("Невозможно сохранить контакт")
    }

    private fun saveContactName(contactId: Long, contactName: String){
        val contentValues = ContentValues().apply{
            put(ContactsContract.Data.RAW_CONTACT_ID, contactId)
            put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactName)
        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
    }

    private fun saveContactPhone(contactId: Long, contactPhone: String){
        val contentValues = ContentValues().apply{
            put(ContactsContract.Data.RAW_CONTACT_ID, contactId)
            put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            put(ContactsContract.CommonDataKinds.Phone.NUMBER, contactPhone)
        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
    }

    private fun saveContactEmail(contactId: Long, contactEmail: String){
        val contentValues = ContentValues().apply{
            put(ContactsContract.Data.RAW_CONTACT_ID, contactId)
            put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
            put(ContactsContract.CommonDataKinds.Email.ADDRESS, contactEmail)
        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
    }

}

