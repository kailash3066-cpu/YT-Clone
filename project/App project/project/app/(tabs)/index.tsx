import { StyleSheet, View, Text, ScrollView } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

export default function HomeScreen() {
  return (
    <SafeAreaView style={styles.safeArea}>
      <ScrollView style={styles.container} contentContainerStyle={styles.content}>
        <View style={styles.header}>
          <Text style={styles.title}>Welcome to Bolt Expo</Text>
          <Text style={styles.subtitle}>Your app is ready for GitHub and Android builds</Text>
        </View>

        <View style={styles.card}>
          <Text style={styles.cardTitle}>Getting Started</Text>
          <Text style={styles.cardText}>
            This app is configured with:
          </Text>
          <Text style={styles.bulletPoint}>• Tab-based navigation</Text>
          <Text style={styles.bulletPoint}>• Supabase integration ready</Text>
          <Text style={styles.bulletPoint}>• GitHub Actions for Android builds</Text>
          <Text style={styles.bulletPoint}>• EAS Build configuration</Text>
        </View>

        <View style={styles.card}>
          <Text style={styles.cardTitle}>Next Steps</Text>
          <Text style={styles.bulletPoint}>1. Run: npx eas-cli init</Text>
          <Text style={styles.bulletPoint}>2. Create Expo account</Text>
          <Text style={styles.bulletPoint}>3. Generate EXPO_TOKEN</Text>
          <Text style={styles.bulletPoint}>4. Add token to GitHub secrets</Text>
          <Text style={styles.bulletPoint}>5. Push to GitHub to start building</Text>
        </View>

        <View style={styles.card}>
          <Text style={styles.cardTitle}>Local Development</Text>
          <Text style={styles.bulletPoint}>• npm run android</Text>
          <Text style={styles.bulletPoint}>• npm run ios</Text>
          <Text style={styles.bulletPoint}>• npm run web</Text>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#fff',
  },
  container: {
    flex: 1,
  },
  content: {
    padding: 16,
  },
  header: {
    marginBottom: 24,
  },
  title: {
    fontSize: 28,
    fontWeight: '700',
    marginBottom: 8,
    color: '#000',
  },
  subtitle: {
    fontSize: 16,
    color: '#666',
    lineHeight: 24,
  },
  card: {
    backgroundColor: '#f5f5f5',
    borderRadius: 12,
    padding: 16,
    marginBottom: 16,
    borderLeftWidth: 4,
    borderLeftColor: '#007AFF',
  },
  cardTitle: {
    fontSize: 18,
    fontWeight: '600',
    marginBottom: 12,
    color: '#000',
  },
  cardText: {
    fontSize: 14,
    color: '#555',
    marginBottom: 8,
    lineHeight: 20,
  },
  bulletPoint: {
    fontSize: 14,
    color: '#555',
    marginBottom: 6,
    marginLeft: 4,
    lineHeight: 20,
  },
});
