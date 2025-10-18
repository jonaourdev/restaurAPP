//package com.example.restaurapp.ui.screens.homeScreen
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material3.Button
//import androidx.compose.material3.DrawerValue
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.ModalDrawerSheet
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.rememberDrawerState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.restaurapp.R
//import com.example.restaurapp.viewmodel.MainViewModel
//import androidx.compose.material3.ModalNavigationDrawer
//import androidx.compose.material3.NavigationDrawerItem
//import com.example.restaurapp.navigation.Screen
//import kotlinx.coroutines.launch
//import androidx.compose.material.icons.filled.Menu
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreenNav(
//    titleSize: Int,
//    imageHeight: Dp,
//    spacing: Dp,
//){
//
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//    val nav = rememberNavController()
//
//    NavHost(navController = nav, startDestination = ""){
//        composable { HomeScreenBase(
//            titleSize,
//            imageHeight,
//            spacing
//        ) }
//    }
////        ModalNavigationDrawer(
////            drawerState = drawerState,
////            drawerContent = {
////                ModalDrawerSheet {
////                    Text("Menú", modifier = Modifier.padding(16.dp))
////                    NavigationDrawerItem(
////                        label = {Text("Ir a perfil")},
////                        selected = false,
////                        onClick = {
////                            scope.launch { drawerState.close() }
////                            vm.navigateTo(Screen.Profile)
////                        }
////                    )
////                    NavigationDrawerItem(
////                        label = {Text("Registro")},
////                        selected = false,
////                        onClick = {
////                            scope.launch { drawerState.close() }
////                            vm.navigateTo(Screen.Register)
////                        }
////                    )
////                }
////            }
////        ){
////            Scaffold(
////                topBar = {
////                    TopAppBar(
////                        title = { Text("RestaurAPP", fontSize = titleSize.sp) },
////                        navigationIcon = {
////                            IconButton(onClick = {
////                                scope.launch { drawerState.open() }
////                            }) {
////                                Icon(Icons.Default.Menu, contentDescription = "Menú")
////                            }
////                        }
////                    )
////                }
////            ) { innerPadding ->
////                Column(
////                    modifier = Modifier.Companion
////                        .padding(innerPadding)
////                        .fillMaxSize()
////                        .padding(16.dp),
////                    verticalArrangement = Arrangement.spacedBy(spacing),
////                    horizontalAlignment = Alignment.Companion.CenterHorizontally
////                ) {
////                    Text(
////                        text = "¡Bienvenido a RestaurApp!",
////                        color = MaterialTheme.colorScheme.primary
////                    )
////                    Button(onClick = { /*ACCION FUTURA*/ }) {
////                        Text("Presióname")
////                    }
////                    Image(
////                        painter = painterResource(id = R.drawable.logo),
////                        contentDescription = "Logo App",
////                        modifier = Modifier.Companion
////                            .fillMaxWidth()
////                            .height(imageHeight),
////                        contentScale = ContentScale.Companion.Fit
////                    )
////                }
////            }
////        }
////    }
//}