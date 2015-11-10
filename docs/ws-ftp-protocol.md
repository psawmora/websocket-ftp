**__WebSocket-File-Transferring-Protoco (Version 1.0.0 -Alpha)__**
-----------------------------------------------------------------

### File Transfer Meta Data Initial Request

 - |Length_of_the_file_URI (4Bytes)|File_URI(Encoded in utf-8)|

### File Part Request - Carries actual file parts

 - |Unique_ID_for_the_file(8Bytes)|Length_of_the_file_part(4Bytes)|part_index(4Bytes)|file_part|

### File Part Ack

 - |Unique_ID_for_the_file(8Bytes)|next_required_part_index(4Bytes)|