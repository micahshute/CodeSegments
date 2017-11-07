//
//  DataController.swift
//  dreamLister
//
//  Created by Micah Shute on 7/26/17.
//  Copyright © 2017 Micah Shute. All rights reserved.
//

import Foundation
import CoreData

class DataController: NSObject{
    
    private var _isSaving: Bool = false
    private var isInitialized: Bool = false
    private static let singletonDataController: DataController = DataController(completionClosure: {})
    
    static func getSharedDataController() -> DataController{
        return .singletonDataController
    }
    
    var persistentContainer: NSPersistentContainer
    
    var managedObjectContext: NSManagedObjectContext {
        get{
            return persistentContainer.viewContext
        }
    }
    
    
    var isSaving: Bool{
        get{
            return _isSaving
        }
    }
   
    private init(completionClosure: @escaping () -> ()){
        
        let container = NSPersistentContainer(name: "Venture")
        container.loadPersistentStores(completionHandler: { (storeDescription, error) in
            if let error = error as NSError? {
                
                return
                //fatalError("Unresolved error \(error), \(error.userInfo)")
            }
            
            completionClosure()
            })
            persistentContainer = container
    }

    
    //MARK: Methods
    
    func saveContext () {
        _isSaving = true
        let context = persistentContainer.viewContext
        if context.hasChanges {
            do {
                try context.save()
            } catch {
                // Replace this implementation with code to handle the error appropriately.
                // fatalError() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
                let nserror = error as NSError
                fatalError("Unresolved error \(nserror), \(nserror.userInfo)")
            }
        }
        _isSaving = false
    }
    
    
}




