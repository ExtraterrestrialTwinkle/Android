curl -X POST -H "Authorization: key=AAAArwN3Tec:APA91bEhGWItDazk3T3IcQ620qF_YUGJC1Vpt2wGuB4nsCXBNOiQyXYyTfITvIIyVot-gyRyN8qKu7SDdqn9c56fNQI6DynX4k4PUYCnPxslkAYAOmnbMSNmHyr6vFF2jiouHGMve194" -H "Content-Type: application/json" -d '{
   "to":"cBycULPbQdmyCUSnC4AfPX:APA91bHkmPRxKF981eAbFipcYBoypUUEfbxkQEeihK3iqzQ5AGyuobRmynabrsjjveq-Kbd7_161v0g_nsnMhMcbvHcFsYa6_5ocs68BA0mxNVWzClxTH5ZpT-AExMBOWLtXwaFti_Wa",
   "data": {
        "type": "event_message",
        "data": {
            "title": "New event!",
            "description": "This is a new event message",
            "image": "https://cs8.pikabu.ru/avatars/2589/x2589965-1082747344.png"
        }
    }
}' https://fcm.googleapis.com/fcm/send -v -i
