# ======= Studentská databáze =======
##  Popis programu

Program slouží ke správě studentů technické univerzity.

***Každý student má:***

-   **Identifikační číslo (ID)**,
    
-   **Jméno**,
    
-   **Příjmení**,
    
-   **Rok narození**,
    
-   **Známky** (hodnocení od 1 do 5).
    

Studenti jsou rozděleni do dvou skupin:

-   **Telekomunikace** – studenti umí převést své jméno a příjmení do Morseovy abecedy,
    
-   **Kyberbezpečnost** – studenti umí vytvořit hash (SHA-256) svého jména a příjmení.
    

----------

## 2. Ovládání programu

Po spuštění programu je uživateli nabídnuto textové menu:
```
=== STUDENTSKÁ DATABÁZE ===
0. Vytvořit databázi (smazat vše a vytvořit znovu)
1. Přidat studenta
2. Přidat známku studentovi
3. Odstranit studenta
4. Najít studenta (ID / jméno / příjmení)
5. Ukázat dovednost studenta
6. Seznam studentů
7. Průměr studentů
8. Počet studentů
9. Uložit studenta do souboru
10. Načíst studenta ze souboru
11. Ukončení programu
```

**Popis funkcí:**
-   **0** — Vytvoření nové databáze v SQLite (smaže všechna existující data).
    
-   **1** — Přidání nového studenta (s kontrolou jména, příjmení a roku narození).
    
-   **2** — Přidání jedné nebo více známek studentovi podle jeho ID.
    
-   **3** — Odstranění studenta podle ID.
    
-   **4** — Vyhledání studenta podle ID, části jména nebo příjmení.
    
-   **5** — Zobrazení dovednosti studenta (Morseova abeceda nebo Hash).
    
-   **6** — Výpis seznamu studentů (všichni / pouze Telekomunikace / pouze Kyberbezpečnost).
    
-   **7** — Výpočet studijního průměru (všichni / pouze Telekomunikace / pouze Kyberbezpečnost).
    
-   **8** — Výpis počtu studentů v jednotlivých oborech.
    
-   **9** — Uložení vybraného studenta do textového souboru.
    
-   **10** — Načtení studenta ze souboru (automaticky přiděleno nové ID).
    
-   **11** — Uložení všech studentů do SQL databáze a ukončení programu.

## 3. Vnitřní architektura programu

Program využívá principy **objektově orientovaného programování (OOP)**.
Struktura projektu:
```less
studentdb/
├── Database.java          // Práce s SQLite databází
├── FileManager.java       // Ukládání a načítání studentů ze souborů
├── Main.java              // Hlavní spouštěcí třída
├── Menu.java              // Uživatelské textové menu
├── Student.java           // Abstraktní třída Student
├── TelecomStudent.java    // ze Student, Morseova abeceda
├── CyberStudent.java      // ze Student, hashování
├── StudentManager.java    // Správa seznamu studentů
├── StudentService.java    // pomocné funkce Menu.java
└── MorseUtil.java         // Převod textu na Morseovu abecedu
```

**Vysvětlení klíčových tříd:**
-   **Student**  
    Abstraktní třída, která definuje společné vlastnosti studentů a obsahuje abstraktní metodu `showSkill()`.
    
-   **TelecomStudent**  
    Student oboru Telekomunikace – převádí své jméno a příjmení do Morseovy abecedy.
    
-   **CyberStudent**  
    Student oboru Kyberbezpečnost – vytváří hash ze svého jména a příjmení.
    
-   **StudentManager**  
    Spravuje seznam studentů (přidávání, mazání, hledání, třídění podle příjmení).  
    Používá dynamickou datovou strukturu `ArrayList<Student>`.
    
-   **StudentService**  
    Zprostředkovává vyšší logiku programu – operace nad studenty podle ID, práce se známkami, kontrola vstupů.
    
-   **Database**  
    Zajišťuje připojení k SQLite databázi. Při spuštění načítá studenty a při ukončení je ukládá zpět.  
    Umí i vytvořit novou databázi ze SQL skriptu.
    
-   **FileManager**  
    Umožňuje ukládání jednotlivých studentů do textového souboru a jejich následné načítání.
    
-   **Menu**  
    Textové rozhraní pro ovládání programu. Nabízí volby uživateli, čte vstupy, zajišťuje plynulý chod.
    
-   **MorseUtil**  
    Pomocná třída pro převod textu do Morseovy abecedy.
    
-   **HashUtil**  
    Pomocná třída pro výpočet hashů (SHA-256) jména a příjmení.

### Speciální techniky použité v programu:

-   **OOP**: dědičnost, abstrakce, polymorfismus (`Student`, `TelecomStudent`, `CyberStudent`).
    
-   **Dynamická datová struktura**: `ArrayList<Student>`.
    
-   **Práce se soubory**: ukládání a načítání studentů z textových souborů.
    
-   **SQL databáze**: načítání a ukládání dat z/do `SQLite` databáze `studentydb.db`.
    
-   **Ošetření chyb**: ochrana proti neplatnému vstupu uživatele (pouze platné známky, kontrola jména a roku narození).
    

----------

## 4. Další poznámky

-   Program **automaticky načte** data z databáze při spuštění.
    
-   Program **automaticky uloží** všechny studenty do databáze při ukončení.
    
-   SQL databáze (`studentydb.db`) obsahuje dvě tabulky: `Student` a `Grade`.
    
-   Textové soubory ukládají jednoho nebo více studentů.
    

----------


