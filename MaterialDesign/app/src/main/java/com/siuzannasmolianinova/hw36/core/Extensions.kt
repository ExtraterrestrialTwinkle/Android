package com.siuzannasmolianinova.hw36.core

import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.siuzannasmolianinova.hw36.R
import com.siuzannasmolianinova.hw36.data.Country
import com.siuzannasmolianinova.hw36.data.db.Category
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun BottomNavigationView.itemSelectedFlow(): Flow<Category> {
    return callbackFlow<Category> {

        val itemSelectedListener =
            NavigationBarView.OnItemSelectedListener { item ->
                when(item.itemId){
                    R.id.page_1 -> {
                        trySendBlocking(Category.GLOBAL)
                        true
                    }
                    R.id.page_2 -> {
                        trySendBlocking(Category.HEALTH)
                        true
                    }
                    R.id.page_3 -> {
                        trySendBlocking(Category.SCIENCE)
                        true
                    }
                    R.id.page_4 -> {
                        trySendBlocking(Category.TECHNOLOGY)
                        true
                    }
                    else -> error("Unexpected Type")
                }
            }

        this@itemSelectedFlow.setOnItemSelectedListener(itemSelectedListener)
        awaitClose {
            this@itemSelectedFlow.setOnItemSelectedListener(null)
        }
    }
}

fun MenuItem.itemClickFlow(): Flow<Country>{
    return callbackFlow {
        val itemClickListener = MenuItem.OnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.ae -> {
                    trySendBlocking(Country.UNITED_ARAB_EMIRATES)
                    true
                }
                R.id.ar -> {
                    trySendBlocking(Country.ARGENTINA)
                    true
                }
                R.id.at -> {
                    trySendBlocking(Country.AUSTRIA)
                    true
                }
                R.id.au -> {
                trySendBlocking(Country.AUSTRALIA)
                true
                }
                R.id.be -> {
                    trySendBlocking(Country.BELGIUM)
                    true
                }
                R.id.bg -> {
                    trySendBlocking(Country.BULGARIA)
                    true
                }
                R.id.br -> {
                    trySendBlocking(Country.BRAZIL)
                    true
                }
                R.id.ca -> {
                    trySendBlocking(Country.CANADA)
                    true
                }
                R.id.ch -> {
                    trySendBlocking(Country.SWITZERLAND)
                    true
                }
                R.id.cn -> {
                    trySendBlocking(Country.CHINA)
                    true
                }
                R.id.co -> {
                    trySendBlocking(Country.COLOMBIA)
                    true
                }
                R.id.cu -> {
                    trySendBlocking(Country.CUBA)
                    true
                }
                R.id.cz -> {
                    trySendBlocking(Country.CZECHIA)
                    true
                }
                R.id.de -> {
                    trySendBlocking(Country.GERMANY)
                    true
                }
                R.id.eg -> {
                    trySendBlocking(Country.EGYPT)
                    true
                }
                R.id.fr -> {
                    trySendBlocking(Country.FRANCE)
                    true
                }
                R.id.gb -> {
                    trySendBlocking(Country.UNITED_KINGDOM)
                    true
                }
                R.id.gr -> {
                    trySendBlocking(Country.GREECE)
                    true
                }
                R.id.hk -> {
                    trySendBlocking(Country.HONG_KONG)
                    true
                }
                R.id.hu -> {
                    trySendBlocking(Country.HUNGARY)
                    true
                }
                R.id.id -> {
                    trySendBlocking(Country.INDONESIA)
                    true
                }
                R.id.ie -> {
                    trySendBlocking(Country.IRELAND)
                    true
                }
                R.id.il -> {
                    trySendBlocking(Country.ISRAEL)
                    true
                }
                R.id.ind -> {
                    trySendBlocking(Country.INDIA)
                    true
                }
                R.id.it -> {
                    trySendBlocking(Country.ITALY)
                    true
                }
                R.id.jp -> {
                    trySendBlocking(Country.JAPAN)
                    true
                }
                R.id.kr -> {
                    trySendBlocking(Country.KOREA)
                    true
                }
                R.id.lt -> {
                    trySendBlocking(Country.LITHUANIA)
                    true
                }
                R.id.lv -> {
                    trySendBlocking(Country.LATVIA)
                    true
                }
                R.id.ma -> {
                    trySendBlocking(Country.MOROCCO)
                    true
                }
                R.id.mx -> {
                    trySendBlocking(Country.MEXICO)
                    true
                }
                R.id.my -> {
                    trySendBlocking(Country.MALAYSIA)
                    true
                }
                R.id.ng -> {
                    trySendBlocking(Country.NIGERIA)
                    true
                }
                R.id.nl -> {
                    trySendBlocking(Country.NETHERLANDS)
                    true
                }
                R.id.no -> {
                    trySendBlocking(Country.NORWAY)
                    true
                }
                R.id.nz -> {
                    trySendBlocking(Country.NEW_ZEALAND)
                    true
                }
                R.id.ph -> {
                    trySendBlocking(Country.PHILIPPINES)
                    true
                }
                R.id.pl -> {
                    trySendBlocking(Country.POLAND)
                    true
                }
                R.id.pt -> {
                    trySendBlocking(Country.PORTUGAL)
                    true
                }
                R.id.ro -> {
                    trySendBlocking(Country.ROMANIA)
                    true
                }
                R.id.rs -> {
                    trySendBlocking(Country.SERBIA)
                    true
                }
                R.id.ru -> {
                    trySendBlocking(Country.RUSSIA)
                    true
                }
                R.id.sa -> {
                    trySendBlocking(Country.SAUDI_ARABIA)
                    true
                }
                R.id.se -> {
                    trySendBlocking(Country.SWEDEN)
                    true
                }
                R.id.sg -> {
                    trySendBlocking(Country.SINGAPORE)
                    true
                }
                R.id.si -> {
                    trySendBlocking(Country.SLOVENIA)
                    true
                }
                R.id.sk -> {
                    trySendBlocking(Country.SLOVAKIA)
                    true
                }
                R.id.th -> {
                    trySendBlocking(Country.THAILAND)
                    true
                }
                R.id.tr -> {
                    trySendBlocking(Country.TURKEY)
                    true
                }
                R.id.tw -> {
                    trySendBlocking(Country.TAIWAN)
                    true
                }
                R.id.ua -> {
                    trySendBlocking(Country.UKRAINE)
                    true
                }
                R.id.us -> {
                    trySendBlocking(Country.USA)
                    true
                }
                R.id.ve -> {
                    trySendBlocking(Country.VENEZUELA)
                    true
                }
                R.id.za -> {
                    trySendBlocking(Country.SOUTH_AFRICA)
                    true
                }
               else -> error("Unexpected id")
            }
        }
        this@itemClickFlow.setOnMenuItemClickListener(itemClickListener)
        awaitClose {
            this@itemClickFlow.setOnMenuItemClickListener(null)
        }
    }
}