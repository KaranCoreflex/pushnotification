/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React, { useEffect, useState } from 'react';
import type {PropsWithChildren} from 'react';
import {
  Button,
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  TextInput,
  useColorScheme,
  View,
} from 'react-native';
import MCReactModule from 'react-native-marketingcloudsdk';

import {
  Colors,
  DebugInstructions,
  Header,
  LearnMoreLinks,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

type SectionProps = PropsWithChildren<{
  title: string;
}>;

function Section({children, title}: SectionProps): React.JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';


  return (
    <View style={styles.sectionContainer}>
      <Text
        style={[
          styles.sectionTitle,
          {
            color: isDarkMode ? Colors.white : Colors.black,
          },
        ]}>
        {title}
      </Text>
      <Text
        style={[
          styles.sectionDescription,
          {
            color: isDarkMode ? Colors.light : Colors.dark,
          },
        ]}>
        {children}
      </Text>
    </View>
  );
}

function App(): React.JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';

  
  const [deviceId, setDeviceId] = useState("");
  const [systemToken, setSystemToken] = useState("");
  const [savedContactKey, setSavedContactKey] = useState("");

  useEffect(() => {
    const contactKey = "003WD00000HOoZFYA1" // Replace with your dynamic key
    setContactKey(contactKey);
  }, []);

  
  const setContactKey = async (contactKey: any) => {
    try {
      await MCReactModule.setContactKey(contactKey);
      console.log('Contact key set successfully');
    } catch (error) {
      console.error('Error setting contact key:', error);
    }
  };

  const fetchDeviceId = async () => {
    try {
      // Call the MCReactModule.getDeviceId() function
      const id: any = await MCReactModule.getDeviceId();
      console.log('device ID:', id);
      setDeviceId(id);
    } catch (error) {
      console.error('Error retrieving device ID:', error);
    }
  };

  const fetchSystemToken = async () => {
    try {
      // Call the MCReactModule.getSystemToken() function
      const token: any = await MCReactModule.getSystemToken();
      console.log('MCReactModule:', MCReactModule);
      console.log('token:', token);
      if (token)
        setSystemToken(token);
    } catch (error) {
      console.error('Error retrieving getSystemToken:', error);
    }
  };

  const getContactKey = () =>{
      MCReactModule.getContactKey()
      .then((key:any) => setSavedContactKey(key))
      .catch((error) => console.error('Error fetching contact key:', error));
  }


  const isPushEnable = async () => {
    try {
      // Call the MCReactModule.getDeviceId() function
      const isPushEnabled: any = await MCReactModule.isPushEnabled();
      console.log("is push enable", isPushEnabled);
      console.log("------------>",isPushEnabled ? "success" : "error", "Push notifications " + (isPushEnabled ? "enabled " : "not enabled"))
    } catch (error) {
      console.error('Error retrieving device ID:->', error);
    }
  };

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <Text style={{marginHorizontal:5, fontWeight:'bold'}}>DeviceId</Text>
      <TextInput multiline numberOfLines={2} value={deviceId} style={{marginHorizontal:10}}></TextInput>
      <Text style={{marginHorizontal:5, fontWeight:'bold'}}>System Token</Text>
      <TextInput multiline numberOfLines={5} value={systemToken} style={{marginHorizontal:10}}></TextInput>
      <Text style={{marginHorizontal:5, fontWeight:'bold'}}>savedContactKey</Text>
      <TextInput multiline numberOfLines={2} value={savedContactKey} style={{marginHorizontal:10}}></TextInput>
      
      <Button title='Get Device Id' onPress={fetchDeviceId} />
      <Button title='Get System Token' onPress={fetchSystemToken} />
      <Button title='pushNotification' onPress={isPushEnable} />
      <Button title='getContactKey' onPress={getContactKey} />
      {/* <Button title="Send Notification" onPress={sendPushNotification} /> */}
      {/* <Button onPress={handleButton} title='Button' /> */}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;
