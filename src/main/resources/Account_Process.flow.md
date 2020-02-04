# DH Account  Process Process Flow Guide

Process Name | Description | SObject | TriggerType | Status
------------ | ----------- | ------- | ----------- | ------
DH Account  Process | Account process | Account | onAllChanges | Active

## Process Variable Definition

These variables represents the old and current instance of the record and process level accessible instances.

Variable | DataType | IsInput | IsOutput | ObjectType | IsCollect
-------- | -------- | ------- | -------- | ---------- | ---------
myVariable-old | SObject | true | false | Account | false
myVariable-current | SObject | true | true | Account | false
myCollection-myRule-1-A1recipientIds | String | false | false |  | true

## Process Flow Execution Detail

The process flow is executed based on the following step sequence.

### Step 1: *isChanged: CleanStatus*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *AND* **Conditions:** **[1]:** *myVariable-old IsNull = false* **[2]:** *myVariable-old . CleanStatus = myVariable-current.CleanStatus* | n/a | Goto Step 2

### Step 2: *isChanged: DHApproval-Path-UI-c*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *AND* **Conditions:** **[1]:** *myVariable-old IsNull = false* **[2]:** *myVariable-old . DHApproval-Path-UI--c = myVariable-current.DHApproval-Path-UI--c* | n/a | Goto Step 3

### Step 3: *isChanged: Type*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *AND* **Conditions:** **[1]:** *myVariable-old IsNull = false* **[2]:** *myVariable-old . Type = myVariable-current.Type* | n/a | Goto Step 4

### Step 4: *isChanged: Type*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *AND* **Conditions:** **[1]:** *myVariable-old IsNull = false* **[2]:** *myVariable-old . Type = myVariable-current.Type* | n/a | Goto Step 5

### Step 5: *Account Process Is Enabaled*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *AND* **Conditions:** **[1]:** *{!$Setup . ProcessAutomationSettings--c . AccountprocessAutomation--c} = false = true* | n/a | Goto Step 6

### Step 6: *Created Or Matched- Data.com*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *AND* **Conditions:** **[1]:** *myVariable-current . CleanStatus = SelectMatch* **[2]:** *isChanged: CleanStatus = true* | **UpdateRecord** *Account* **Action1**: *TPM-LevelType--c = Not Trade DN--OneHubAccountType--c = Retailer MDM-LevelType--c = Owning Company Type = Retailer MDM-IsClient--c = false MDM-IsCustomer--c = true RecordTypeId = 01241000001cn6aAAA* | Goto Step 7

### Step 7: *Requires Legal Changed*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *AND* **Conditions:** **[1]:** *myVariable-current . DN--CanRequestProjects--c = true* | n/a | Goto Step 8

### Step 8: *Record Type Changed to Approved*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *OR* **Conditions:** **[1]:** *myVariable-current . RecordTypeId = 01241000001cn6MAAQ* **[2]:** *myVariable-current . RecordTypeId = 01241000001cn6OAAQ* | n/a | Goto Step 9

### Step 9: *Only approved account*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *1 AND 3 AND 4 AND (2 OR 5 OR 6 OR 7)* **Conditions:** **[1]:** *myVariable-current . DN--IsApproved--c = true* **[2]:** *myVariable-current . RecordTypeId = 01241000001cn6MAAQ* **[3]:** *myVariable-current . DHAdd-Community-Chatter-Group--c = true* **[4]:** *myVariable-current . CH-Collaboration-Group-Id--c IsNull = true* **[5]:** *myVariable-current . RecordTypeId = 01241000001cn6OAAQ* **[6]:** *myVariable-current . RecordTypeId = 012410000015m9RAAQ* **[7]:** *myVariable-current . RecordTypeId = 01241000001cn6LAAQ* | n/a | Goto Step 10

### Step 10: *Account must be on Hold*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *AND* **Conditions:** **[1]:** *myVariable-current . DHApproval-Path-UI--c = On Hold* **[2]:** *isChanged: DHApproval-Path-UI-c = true* | **Action1:** *chatterPost* -> *chatterPost* -> *Chatter Update* | Goto Step 11

### Step 11: *Legal Required*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *AND* **Conditions:** **[1]:** *myVariable-current . DHAccounting-Confirmed--c = true* **[2]:** *myVariable-current . DHRequire-Legal-Confirmation--c = true* **[3]:** *myVariable-current . DHProceed-to-Legal-Confirmation-Gate--c = true* | n/a | Goto Step 12

### Step 12: *Legal Not Required*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *AND* **Conditions:** **[1]:** *myVariable-current . DHAccounting-Confirmed--c = true* **[2]:** *myVariable-current . DHRequire-Legal-Confirmation--c = false* **[3]:** *myVariable-current . DHProceed-to-Legal-Confirmation-Gate--c = true* | n/a | Goto Step 13

### Step 13: *Unapproved Trying to Set as Appproved*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *AND* **Conditions:** **[1]:** *myVariable-current . DN--IsApproved--c = false* **[2]:** *myVariable-current . RecordTypeId = 01241000001cn6MAAQ* | **UpdateRecord** *Account* **Action1**: *RecordTypeId = 01241000001cn6NAAQ* | Goto Step 14

### Step 14: *Unapproved Retailer Trying to Set as Approved*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *AND* **Conditions:** **[1]:** *myVariable-current . DN--IsApproved--c = true* **[2]:** *myVariable-current . RecordTypeId = 01241000001cn6OAAQ* | **UpdateRecord** *Account* **Action1**: *RecordTypeId = 01241000001cn6aAAA* | Goto Step 15

### Step 15: *Data Quality Changed*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *AND* **Conditions:** **[1]:** *myVariable-current . MDM-Data-Quality-Confirmed--c = true* | **UpdateRecord** *Account* **Action1**: *DHApproval-Path-UI--c = Data Quality Confirmed* | Goto Step 16

### Step 16: *Data Quality Check Null Path*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *AND* **Conditions:** **[1]:** *myVariable-current . MDM-Data-Quality-Confirmed--c = true* | **UpdateRecord** *Account* **Action1**: *DHApproval-Path-UI--c = Data Quality Confirmed* | Goto Step 17

### Step 17: *Is Manufacturer*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *2 AND 6 AND 7 AND ( 1 OR 3 OR 4 OR 5 )* **Conditions:** **[1]:** *myVariable-current . Type = Manufacturer* **[2]:** *isChanged: Type = true* **[3]:** *myVariable-current . Type = Cooperative* **[4]:** *myVariable-current . Type = Holding Company* **[5]:** *myVariable-current . Type = Other* **[6]:** *myVariable-current . MDM-LevelType--c = Not Mastered* **[7]:** *myVariable-current . DN--IsApproved--c = false* | **UpdateRecord** *Account* **Action1**: *DN--OneHubAccountType--c = Manufacturer MDM-IsClient--c = true* | Exit Of Process

### Step 18: *Is Retailer*

Condition | Action | Then
--------- | ------ | ----
**Condition Logic:** *1 AND 5 AND 6 AND ( 2 OR 3 OR 4 )* **Conditions:** **[1]:** *isChanged: Type = true* **[2]:** *myVariable-current . Type = Retailer* **[3]:** *myVariable-current . Type = Wholesaler* **[4]:** *myVariable-current . Type = Distributor* **[5]:** *myVariable-current . MDM-LevelType--c = Not Mastered* **[6]:** *myVariable-current . DN--IsApproved--c = false* | **UpdateRecord** *Account* **Action1**: *DN--OneHubAccountType--c = Retailer MDM-IsCustomer--c = true* | Exit Of Process