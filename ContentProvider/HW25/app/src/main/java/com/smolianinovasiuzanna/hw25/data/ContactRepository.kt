package com.smolianinovasiuzanna.hw25.data

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class ContactsRepository(private val context: Context) {

    //private val phonePattern = Pattern.compile("^\\+?[0-9]{3}?[0-9]{6,12}\$")

    private var contactList = mutableListOf<Contact>()
    private val contactsNumberMap = HashMap<Long, ArrayList<String>>()// создаю HashMap телефонных номеров, ключ - ID
    private  val contactsEmailsMap = HashMap<Long, ArrayList<String>>()// создаю HashMap телефонных номеров, ключ - ID

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
            val currentContact = Contact(id = id, name = name)
            if(contactList.contains(currentContact).not()){
                contactList.add(currentContact)
            }
        } while (cursor.moveToNext())
        Log.d("ContactList", contactList.size.toString())
        return contactList
    }

    suspend fun getPhoneNumbers(): HashMap<Long, ArrayList<String>> {
        Log.d("ContactsRepository", "getPhoneNumbersForContact")
        withContext(Dispatchers.IO){
            context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )?.use { phoneCursor -> // Получаю данные о телефонных номерах от курсора
                if(phoneCursor.moveToFirst().not()) return@use

                val contactIdIndex =
                        phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                    val numberIndex =
                        phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    do {
                        val contactId = phoneCursor.getLong(contactIdIndex)
                        val number: String = phoneCursor.getString(numberIndex)
                        //Проверка содержит ли мапа ключ, если нет - создается новый array list с номером
                        if (contactsNumberMap.containsKey(contactId)) {
                            if(contactsNumberMap[contactId]?.contains(number) == false)
                            contactsNumberMap[contactId]?.add(number)
                        } else {
                            contactsNumberMap[contactId] = arrayListOf(number)
                        }
                    } while (phoneCursor.moveToNext())
                    //контакт содержит все номера
                }
        }
        return contactsNumberMap
    }

    suspend fun getEmails(): HashMap<Long, ArrayList<String>>{
        Log.d("ContactsRepository", "getEmailsForContact")
        withContext(Dispatchers.IO){
            context.contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                null,
                null,
                null
            )?.use { emailCursor -> // Получаю данные об электронных адресах от курсора
                if(emailCursor.moveToFirst().not()) return@use

                    val contactIdIndex =
                        emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID)
                    val addressIndex =
                        emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                    do {
                        val contactId = emailCursor.getLong(contactIdIndex)
                        val email: String = emailCursor.getString(addressIndex)
                        //Проверка содержит ли мапа ключ, если нет - создается новый array list с адресом
                        if (contactsEmailsMap.containsKey(contactId)) {
                            if(contactsEmailsMap[contactId]?.contains(email) == false)
                            contactsEmailsMap[contactId]?.add(email)
                        } else {
                            contactsEmailsMap[contactId] = arrayListOf(email)
                        }
                    } while (emailCursor.moveToNext())
                    //контакт содержит все адреса
                }
        }
        return contactsEmailsMap
    }

    fun getDetailContactInfo(contactId: Long): Contact {
        return contactList.find { contact ->
            contact.id == contactId
        } ?: error("No contact")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun deleteContact(contactId: Long): List<Contact>{
        withContext(Dispatchers.IO) {
            val contactUri = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI,
                contactId
            )
            context.contentResolver.delete(contactUri, null, null)
        }
        val remove = contactList.removeIf{it.id == contactId}
        contactsNumberMap.remove(contactId)
        contactsEmailsMap.remove(contactId)
        Log.d("deleteContact", "remove = $remove")
        return contactList.sortedBy { it.name }
    }

    suspend fun addContact(name: String, phones: ArrayList<String>, emails: ArrayList<String>): List<Contact>{

        return withContext(Dispatchers.IO){
            val contactId = saveRawContact()
            Log.d("newContact", "id = $contactId")
            saveContactName(contactId, name)
            saveContactPhone(contactId, phones)
            if (emails.isNotEmpty()){
                saveContactEmail(contactId, emails)
            }
            val newContact = Contact(contactId, name)
            Log.d("Contact list before adding","size = ${contactList.size}")
            contactList.add(newContact)
            Log.d("Contact list after adding","size = ${contactList.size}")
            contactList.sortedBy { it.name }
        }
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

    private fun saveContactPhone(contactId: Long, contactPhone: ArrayList<String>){
        val contentValues = ContentValues().apply{
            put(ContactsContract.Data.RAW_CONTACT_ID, contactId)
            put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            for (i in contactPhone.indices) {
                put(ContactsContract.CommonDataKinds.Phone.NUMBER, contactPhone[i])
            }
        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
        contactsNumberMap[contactId] = contactPhone
    }

    private fun saveContactEmail(contactId: Long, contactEmail: ArrayList<String>){
        val contentValues = ContentValues().apply{
            put(ContactsContract.Data.RAW_CONTACT_ID, contactId)
            put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
            for(i in contactEmail.indices) {
                put(ContactsContract.CommonDataKinds.Email.ADDRESS, contactEmail[i])
            }
        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
        contactsEmailsMap[contactId] = contactEmail
    }
}
