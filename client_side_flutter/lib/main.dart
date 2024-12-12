import 'package:flutter/material.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Christmas Shopping List',
      theme: ThemeData(
        primarySwatch: Colors.red,
      ),
      home: const ShoppingListPage(),
    );
  }
}

class ShoppingListPage extends StatefulWidget {
  const ShoppingListPage({super.key});

  @override
  _ShoppingListPageState createState() => _ShoppingListPageState();
}

class _ShoppingListPageState extends State<ShoppingListPage> {
  final String apiUrl = const String.fromEnvironment(
    'SPRING_BOOT_API_URL',
    defaultValue: 'https://literate-tribble-pjp6wr65vj4rhr65w-8080.app.github.dev/api/shoppingItems',
  );

  List<dynamic> items = [];
  bool isLoading = true;
  final TextEditingController nameController = TextEditingController();
  final TextEditingController amountController = TextEditingController();

  @override
  void initState() {
    super.initState();
    fetchItems();
  }

  Future<void> fetchItems() async {
    setState(() => isLoading = true);
    try {
      final response = await http.get(Uri.parse(apiUrl));
      if (response.statusCode == 200) {
        setState(() {
          items = json.decode(response.body);
        });
      }
    } catch (e) {
      debugPrint('Error fetching items: $e');
    } finally {
      setState(() => isLoading = false);
    }
  }

  Future<void> addItem(String name, int amount) async {
    final payload = jsonEncode({"name": name, "amount": amount});
    await http.post(
      Uri.parse(apiUrl),
      headers: {"Content-Type": "application/json"},
      body: payload,
    );
    fetchItems();
  }

  Future<void> deleteItem(String name) async {
    await http.delete(Uri.parse('$apiUrl/$name'));
    fetchItems();
  }

  void showAddItemDialog() {
    nameController.clear();
    amountController.clear();
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Add Shopping Item'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(
              controller: nameController,
              decoration: const InputDecoration(labelText: 'Name'),
            ),
            TextField(
              controller: amountController,
              decoration: const InputDecoration(labelText: 'Amount'),
              keyboardType: TextInputType.number,
            ),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('Cancel'),
          ),
          ElevatedButton(
            onPressed: () {
              final name = nameController.text;
              final amount = int.tryParse(amountController.text) ?? 1;
              addItem(name, amount);
              Navigator.of(context).pop();
            },
            child: const Text('Add'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Christmas Shopping List'),
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
              itemCount: items.length,
              itemBuilder: (context, index) {
                final item = items[index];
                return ListTile(
                  title: Text(item['name']),
                  subtitle: Text('Amount: ${item['amount']}'),
                  trailing: IconButton(
                    icon: const Icon(Icons.delete, color: Colors.red),
                    onPressed: () => deleteItem(item['name']),
                  ),
                );
              },
            ),
      floatingActionButton: FloatingActionButton(
        onPressed: showAddItemDialog,
        child: const Icon(Icons.add),
      ),
    );
  }
}
