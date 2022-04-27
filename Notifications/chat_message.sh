curl -X POST -H "Authorization: key=AAAArwN3Tec:APA91bEhGWItDazk3T3IcQ620qF_YUGJC1Vpt2wGuB4nsCXBNOiQyXYyTfITvIIyVot-gyRyN8qKu7SDdqn9c56fNQI6DynX4k4PUYCnPxslkAYAOmnbMSNmHyr6vFF2jiouHGMve194" -H "Content-Type: application/json" -d '{
   "to":"cBycULPbQdmyCUSnC4AfPX:APA91bHkmPRxKF981eAbFipcYBoypUUEfbxkQEeihK3iqzQ5AGyuobRmynabrsjjveq-Kbd7_161v0g_nsnMhMcbvHcFsYa6_5ocs68BA0mxNVWzClxTH5ZpT-AExMBOWLtXwaFti_Wa",
   "data": {
        "type": "chat_message",
        "data": {
            "user_name": "Ivan Ivanov",
            "user_id": "204857",
            "text": "Hi",
            "created_at": {{$isoTimestamp}}
        }
    }
}' https://fcm.googleapis.com/fcm/send -v -i