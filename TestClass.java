import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tala
 */
public class TestClass {
    
    protected static ArrayList<Supplier> supplierList;
    protected static ArrayList<Store> StoreList;
    protected static ArrayList<Crop> CropList;
    
    public static void readSuppliers() throws IdDublicateException {
        File suppliersFile = new File("Suppliers.txt");
        try {
            Scanner scn = new Scanner(suppliersFile);
            while (scn.hasNextLine()) {
                String supplier = scn.nextLine();
                String[] suppliers = supplier.split(", ");
                int ID = Integer.parseInt(suppliers[1]);
                CheckIdDublicate(ID);
                supplierList.add(new Supplier(suppliers[0], Integer.parseInt(suppliers[1]), Integer.parseInt(suppliers[2])));
            }
            scn.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }    
    
    public static void readCrops(){
        File cropFile = new File("Crops.txt");
        try {
            Scanner scn = new Scanner(cropFile);
            while (scn.hasNextLine()) {
                String crop = scn.nextLine();
                String[] crops = crop.split(", ");
                Crop crop1;
                {
                //    if (CropList.isEmpty()) {
                        if (crops[1].equals("fruit")) {
                            crop1 = new Fruit(crops[0], Integer.parseInt(crops[2]), crops[3], crops[4], Integer.parseInt(crops[5]));
                            CropList.add(crop1);
                        } else {
                            crop1 = new Vegetable(crops[0], Integer.parseInt(crops[2]), crops[3]);
                            CropList.add(crop1);
                        }


                }
                String IDString = (crops[crops.length - 1]);
                int ID = Integer.parseInt(IDString);
                for (Supplier supplier : supplierList) {
                    if (supplier.getID() == ID) {
                        supplier.cropList.add(crop1);
                    }
                }
                for (Store store : StoreList) {
                    if (store.getID() == ID) {
                        store.getFruitList().add((Fruit) crop1);
                    }
                }
            }

            scn.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }    
    }
    
    public static void CheckIdDublicate (int ID) throws IdDublicateException{
        int A;
        A = ID;
        while(A > 10){
            A /= 10;
        }
        if (A == 1) {
            for (Supplier supplier : supplierList) {
                if (supplier.getID() == ID) {
                    throw new IdDublicateException();
                }
            }
        }
        if (A == 5) {
            for (Store store : StoreList) {
                if (store.getID() == ID) {
                    throw new IdDublicateException();
                }
            }
        }
    } 


    public static void readStores() throws IdDublicateException {
        File storeFile = new File("Stores.txt");
        try {
            Scanner scn = new Scanner(storeFile);
            while (scn.hasNextLine()) {
                String store = scn.nextLine();
                String[] stores = store.split(", ");
                int ID = Integer.parseInt(stores[1]);
                CheckIdDublicate(ID);                
                StoreList.add(new Store(stores[0], Integer.parseInt(stores[1]), Integer.parseInt(stores[2]), Integer.parseInt(stores[3])));
            }
            scn.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public static void main(String[] args) throws CanNotBeStoredException, CapacityNotEnoughException, FruitNotAvailableException, FruitNotFoundException, SupplierHasNotEnougMoneyException, IdDublicateException {

        StoreList = new ArrayList<Store>();
        supplierList = new ArrayList<Supplier>();
        CropList = new ArrayList<Crop>();
        readSuppliers();
        readStores();
        readCrops();

        Scanner scn = new Scanner(System.in);
        int action = -1;
        for (Crop value : CropList) {
            System.out.println(value);
        }
        System.out.println(supplierList.size() + " " + StoreList.size() + " " + CropList.size() );
        while (action != 0) {
            System.out.println("___________________________________________________________");
            System.out.println("Agricultural Management System:\nMain menu:  ");
            System.out.println("1. Display all suppliers\n2. Display all stores\n3. Buy a fruit crop\n4. Sell a fruit crop\n5. Remove a fruit from a store\n6. Remove a crop from a supplier \n7. Add crop\n8. Show remaining budget\n9. Show remaining capacity\n0. Quit  ");
            System.out.print("Please make a choice(0 to 9): ");
            action = scn.nextInt();

            switch (action) {
                case 1:
                    int x = 1;   
                    for (Supplier i :supplierList) {
                        System.out.println(i.getName());
                        System.out.println("\tCrops:");
                        for (Crop j:i.getCropList()){
                            System.out.print("\t\t" + x + ") " + j.name +": "+i.howToStore(j)+", and "+j.consumeIt() );
                            x++;
                            System.out.println(" ");
                        }
                        x=1;
                        System.out.println("\t\t------------------");
                    }   break;
                case 2:
                    x = 1;
                    for (Store i: StoreList) {
                        System.out.println(i.getName());
                        System.out.println("\tFruits: ");
                        for(Fruit j:i.getFruitList()){
                            System.out.println("\t\t" + x + ") " + j.name + ": " +i.howToStore(j) + ", and " + j.consumeIt());
                            x++;
                            System.out.println(" ");
                        }
                        x=1;
                        System.out.println("\t\t------------------");                        
                    }   break;
                case 3:
                     int supplier;
                     int crop;
                     int store;
                    try {
                        System.out.println("Choose the supplier by typing the number: ");
                        for (int i = 0; i < supplierList.size(); i++) {
                           System.out.println("Number: " + i + ", Supplier name: " + supplierList.get(i).getName()); 
                        }
                        supplier = scn.nextInt();
                } catch (IndexOutOfBoundsException e) {
                        System.out.println("An error accured (IndexOutOfBoundsException)");
                        System.out.println("Enter another number: ");
                        supplier = scn.nextInt();
                }
                    try {
                        System.out.println("Choose the crop that you want to buy by typing the number:");
                        for (int i = 0; i < CropList.size(); i++) {
                            System.out.println("Number: " + i + ", Crop name: " + CropList.get(i).name);
                        }  
                        crop = scn.nextInt();
                } catch (IndexOutOfBoundsException e) {
                        System.out.println("An error accured (IndexOutOfBoundsException)");
                        System.out.println("Enter another number: ");
                        crop = scn.nextInt();                        
                }
                    try {
                        System.out.println("Choose the store that you want to buy from by typing the number: ");
                        for (int i = 0; i < StoreList.size(); i++) {
                            System.out.println("Number: " + i + ", Store name: " + StoreList.get(i).name);
                        }
                        store = scn.nextInt();
                } catch (IndexOutOfBoundsException e) {
                        System.out.println("An error accured (IndexOutOfBoundsException)");
                        System.out.println("Enter another number: ");
                        store = scn.nextInt();    
                }
                    supplierList.get(supplier).buyCrop(CropList.get(crop), StoreList.get(store));
                    break;
                case 4:
                    try {
                        System.out.println("Choose the supplier by typing the number: ");
                        for (int i = 0; i < supplierList.size(); i++) {
                           System.out.println("Number: " + i + ", Supplier name: " + supplierList.get(i).getName()); 
                        }
                        supplier = scn.nextInt();
                } catch (IndexOutOfBoundsException e) {
                        System.out.println("An error accured (IndexOutOfBoundsException)");
                        System.out.println("Enter another number: ");
                        supplier = scn.nextInt();
                }
                    try {
                        System.out.println("Choose the crop that you want to sell by typing the number:");
                        for (int i = 0; i < CropList.size(); i++) {
                            System.out.println("Number: " + i + ", Crop name: " + CropList.get(i).name);
                        }  
                        crop = scn.nextInt();
                } catch (IndexOutOfBoundsException e) {
                        System.out.println("An error accured (IndexOutOfBoundsException)");
                        System.out.println("Enter another number: ");
                        crop = scn.nextInt();                        
                }
                    try {
                        System.out.println("Choose the store that you want to sell from by typing the number: ");
                        for (int i = 0; i < StoreList.size(); i++) {
                            System.out.println("Number: " + i + ", Store name: " + StoreList.get(i).name);
                        }
                        store = scn.nextInt();
                } catch (IndexOutOfBoundsException e) {
                        System.out.println("An error accured (IndexOutOfBoundsException)");
                        System.out.println("Enter another number: ");
                        store = scn.nextInt();    
                }
                    supplierList.get(supplier).sellCrop(CropList.get(crop), StoreList.get(store));
                    break;
                case 5:
                    int fruit;
                    try {
                        System.out.println("Choose the store that you want to remove the fruit from by typing the number: ");
                        for (int i = 0; i < StoreList.size(); i++) {
                            System.out.println("Number: " + i + ", Store name: " + StoreList.get(i).name);
                        }
                        store = scn.nextInt();
                } catch (IndexOutOfBoundsException e) {
                        System.out.println("An error accured (IndexOutOfBoundsException)");
                        System.out.println("Enter another number: ");
                        store = scn.nextInt();    
                }
                    try {
                        System.out.println("Choose the fruit that you want to remove by typing the number: ");
                        for (int i = 0; i < StoreList.get(store).fruitList.size(); i++) {
                            System.out.println("Number: " + i + ", Fruit name: " + StoreList.get(store).fruitList.get(i));
                        }
                         fruit = scn.nextInt();
                } catch (IndexOutOfBoundsException e) {
                        System.out.println("An error accured (IndexOutOfBoundsException)");
                        System.out.println("Enter another number: ");
                        fruit = scn.nextInt();    
                }
                    StoreList.get(store).exportCrop(StoreList.get(store).fruitList.get(fruit));
                    break;
                case 6:
                    try {
                        System.out.println("Choose the supplier that you want to remove the crop from by typing the number: ");
                        for (int i = 0; i < supplierList.size(); i++) {
                            System.out.println("Number: " + i + ", Supplier name: " + supplierList.get(i).getName());
                        }
                        supplier = scn.nextInt();
                } catch (IndexOutOfBoundsException e) {
                        System.out.println("An error accured (IndexOutOfBoundsException)");
                        System.out.println("Enter another number: ");
                        supplier = scn.nextInt();    
                }
                    try {
                        System.out.println("Choose the crop that you want to remove by typing the number: ");
                        for (int i = 0; i < supplierList.get(supplier).cropList.size(); i++) {
                            System.out.println("Number: " + i + ", Crop name: " + supplierList.get(supplier).cropList.get(i));
                        }
                         crop = scn.nextInt();
                } catch (IndexOutOfBoundsException e) {
                        System.out.println("An error accured (IndexOutOfBoundsException)");
                        System.out.println("Enter another number: ");
                        crop = scn.nextInt();    
                }
                    supplierList.get(supplier).cropList.remove(crop);
                    break;
                case 7:
                    Vegetable vegetable = null;
                        Fruit newFruit = null;
                        System.out.println("Choose the type of the crop that you want to buy by typing the number:\n1) Fruit\t2) Vegetable");
                         crop = scn.nextInt();
                             if (crop == 1) {
                        System.out.println("Enter the name of the fruit: ");
                        String nameOfFruit = scn.next();
                        System.out.println("Enter the weight of the fruit: ");
                        int weight = scn.nextInt();
                        System.out.println("Enter the cultivated season: ");
                        String cvs = scn.next();
                        System.out.println("Enter the taste: ");
                        String taste = scn.next();
                        System.out.println("Enter the price: ");
                        int price = scn.nextInt();
                        newFruit = new Fruit(nameOfFruit, weight, cvs, taste, price);
                        System.out.println("Choose where you want to add it by typing the number:\n1) store\t2) Supplier");
                    int choose = scn.nextInt();                        
                     if (choose == 1) {
                        System.out.println("Choose the store: ");
                        for (int i = 0; i < StoreList.size(); i++) {
                            System.out.println("Number: " + i + ", Store Name: " + StoreList.get(i).name);
                        }
                        store = scn.nextInt();
                        newFruit.storeIt(StoreList.get(store));
                     }else if (choose == 2) {
                        System.out.println("Choose the supplier: ");
                        for (int i = 0; i < supplierList.size(); i++) {
                            System.out.println("Number: " + i + ", Supplier Name: " +supplierList.get(i).getName());
                        }
                        supplier = scn.nextInt();
                        supplierList.get(supplier).cropList.add(newFruit);
                    }
                    else System.out.println("Enter 1 to add in store or 2 to add for a supplier: ");
                     choose = scn.nextInt();
                }
                    else if(crop == 2){
                        System.out.println("Enter the name of the vegetable: ");
                        String nameOfVegetable = scn.next();
                        System.out.println("Enter the weight of the vegetable: ");
                        int weight = scn.nextInt();
                        System.out.println("Enter the city name: ");
                        String cityName = scn.next();
                 vegetable = new Vegetable(nameOfVegetable, weight, cityName);
                    }
                    
                    System.out.println("Choose where you want to add it by typing the number:\n1) store\t2) Supplier");
                int choose = scn.nextInt();                        
                     if (choose == 1) {
                        System.out.println("Choose the store: ");
                        for (int i = 0; i < StoreList.size(); i++) {
                            System.out.println("Number: " + i + ", Store Name: " + StoreList.get(i).name);
                            
                        }
                        store = scn.nextInt();
                        vegetable.storeIt(StoreList.get(store));
                    }else if (choose == 2) {
                        System.out.println("Choose the supplier: ");
                        for (int i = 0; i < supplierList.size(); i++) {
                            System.out.println("Number: " + i + ", Supplier Name: " +supplierList.get(i).getName());
                        }
                        supplier = scn.nextInt();
                        supplierList.get(supplier).cropList.add(vegetable);
                    }
                    break;


                case 8:
                    try {
                        for (int i = 0; i < supplierList.size(); i++) {
                            System.out.println("Number: " + i + " ,Supplier name: " + supplierList.get(i).getName());
                        }
                        System.out.println("Enter the number of the supplier that you want to know the remaining budget for: ");
                        int supplierNum = scn.nextInt();
                        System.out.println("Remaining budget: " + supplierList.get(supplierNum).getBudget());
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("An error accured (IndexOutOfBoundsException)");
                        System.out.println("Enter another number: ");
                        int supplierNum = scn.nextInt();
                        System.out.println("Remaining budget: " + supplierList.get(supplierNum).getBudget());
                    }   break;
                case 9:
                    try {
                        for (int i = 0; i < StoreList.size(); i++) {
                            System.out.println("Number: " + i + " ,Store name: " + StoreList.get(i).name);
                        }
                        System.out.println("Enter the number of the store that you want to know the remaining capacity of: ");
                        int storeNum = scn.nextInt();
                        System.out.println("Remaining capacity: " + StoreList.get(storeNum).availabeCapacity());
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("An error accured (IndexOutOfBoundsException)");
                        System.out.println("Enter another number: ");
                        int storeNum = scn.nextInt();
                        System.out.println("Remaining capacity: " + StoreList.get(storeNum).availabeCapacity());
                    }   break;
                case 0:
                    System.out.println("Ok, good bye.");
                    break;
                default:
                    break;
            }
        }

    }
}


