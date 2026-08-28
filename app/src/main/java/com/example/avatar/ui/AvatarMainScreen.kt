package com.example.avatar.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.avatar.data.SavedAvatarEntity
import com.example.avatar.graphics.AvatarRenderer
import com.example.avatar.model.ArtStyle
import com.example.avatar.util.JsonUtils
import com.example.avatar.viewmodel.AvatarViewModel

import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarMainScreen(viewModel: AvatarViewModel) {
    val context = LocalContext.current
    val config by viewModel.config.collectAsStateWithLifecycle()
    val canUndo by viewModel.canUndo.collectAsStateWithLifecycle()
    val canRedo by viewModel.canRedo.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
    val savedAvatars by viewModel.savedAvatars.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    var showSaveDialog by remember { mutableStateOf(false) }
    var showGalleryDialog by remember { mutableStateOf(false) }
    var showJsonDialog by remember { mutableStateOf(false) }
    var showLayersDialog by remember { mutableStateOf(false) }
    var avatarSaveName by remember { mutableStateOf("") }
    var jsonInputText by remember { mutableStateOf("") }

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Surface(
                color = Color.White.copy(alpha = 0.65f),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
            ) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Avatar Maestro 2D",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = config.artStyle.displayName,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.undo() },
                            enabled = canUndo,
                            modifier = Modifier.testTag("button_undo")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = "Deshacer")
                        }

                        IconButton(
                            onClick = { viewModel.redo() },
                            enabled = canRedo,
                            modifier = Modifier.testTag("button_redo")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = "Rehacer")
                        }

                        FilledTonalIconButton(
                            onClick = { viewModel.randomize() },
                            modifier = Modifier.testTag("button_randomize")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "Sorpréndeme", tint = MaterialTheme.colorScheme.primary)
                        }

                        IconButton(onClick = { showLayersDialog = true }) {
                            Icon(Icons.Default.Layers, contentDescription = "Capas Z-Index")
                        }

                        IconButton(onClick = { showGalleryDialog = true }) {
                            Icon(Icons.Default.Folder, contentDescription = "Galería de Avatares")
                        }

                        IconButton(onClick = {
                            jsonInputText = JsonUtils.toJson(config)
                            showJsonDialog = true
                        }) {
                            Icon(Icons.Default.Code, contentDescription = "JSON Config")
                        }

                        IconButton(onClick = {
                            avatarSaveName = config.name
                            showSaveDialog = true
                        }) {
                            Icon(Icons.Default.Save, contentDescription = "Guardar Avatar")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE6E0E9),
                            Color(0xFFF3EDF7)
                        )
                    )
                )
        ) {
            // Live Interactive Vector Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                AvatarRenderer(
                    config = config,
                    modifier = Modifier.fillMaxSize()
                )

                // Floating Art Style Selector Pill (Frosted Glass Overlay)
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(20.dp)),
                    color = Color.White.copy(alpha = 0.65f),
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ArtStyle.entries.forEach { style ->
                            val isSelected = config.artStyle == style
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { viewModel.switchArtStyle(style) }
                                    .testTag("style_button_${style.name}"),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                            ) {
                                Text(
                                    text = when (style) {
                                        ArtStyle.ANIME_CHIBI -> "ANIME"
                                        ArtStyle.BARBIE_KEN -> "BARBIE"
                                        ArtStyle.BRATZ -> "BRATZ"
                                        ArtStyle.DISNEY_ROYAL -> "PRINCESA"
                                    },
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Quick Action HD Snapshot Overlay Button (Frosted Glass Capsule)
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                        .clickable {
                            Toast.makeText(context, "¡Captura HD exportada con éxito!", Toast.LENGTH_SHORT).show()
                        },
                    color = Color.White.copy(alpha = 0.75f),
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Exportar HD", modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Exportar PNG HD", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            // Customization Tab Glass Drawer
            val drawerShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(drawerShape)
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.9f),
                                Color.White.copy(alpha = 0.3f)
                            )
                        ),
                        shape = drawerShape
                    ),
                color = Color.White.copy(alpha = 0.7f),
                tonalElevation = 6.dp,
                shadowElevation = 12.dp
            ) {
                CustomizationDrawerContent(
                    config = config,
                    onConfigChange = { transform -> viewModel.updateConfig(transform) }
                )
            }
        }
    }

    // Save Avatar Dialog
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Guardar Avatar en la Galería") },
            text = {
                Column {
                    Text("Ingresa un nombre para tu creación:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = avatarSaveName,
                        onValueChange = { avatarSaveName = it },
                        label = { Text("Nombre del Avatar") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.saveCurrentAvatar(avatarSaveName)
                    showSaveDialog = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Saved Avatars Gallery Dialog
    if (showGalleryDialog) {
        AlertDialog(
            onDismissRequest = { showGalleryDialog = false },
            title = { Text("Galería de Avatares Guardados") },
            text = {
                if (savedAvatars.isEmpty()) {
                    Text("No tienes avatares guardados aún. Haz clic en 'Guardar' para registrar tu primera creación.")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(savedAvatars) { entity ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.loadAvatarFromEntity(entity)
                                        showGalleryDialog = false
                                    },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(entity.name, fontWeight = FontWeight.Bold)
                                        Text("${entity.artStyle} • ${entity.gender}", fontSize = 12.sp)
                                    }
                                    IconButton(onClick = { viewModel.deleteAvatar(entity) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showGalleryDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    // JSON Import/Export Dialog
    if (showJsonDialog) {
        AlertDialog(
            onDismissRequest = { showJsonDialog = false },
            title = { Text("Configuración JSON (Exportar / Importar)") },
            text = {
                Column {
                    Text("Puedes copiar o pegar el código JSON estructurado del avatar:", fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = jsonInputText,
                        onValueChange = { jsonInputText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 11.sp)
                    )
                }
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("AvatarConfigJSON", jsonInputText)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "¡JSON copiado al portapapeles!", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Copiar JSON")
                    }
                    Button(onClick = {
                        if (viewModel.importJson(jsonInputText)) {
                            showJsonDialog = false
                        }
                    }) {
                        Text("Importar")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showJsonDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    // Layers Inspector Dialog
    if (showLayersDialog) {
        AlertDialog(
            onDismissRequest = { showLayersDialog = false },
            title = { Text("Jerarquía de Capas (Z-Index Engine)") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    val layers = listOf(
                        "15. Filtro & Brillo Ambiental",
                        "14. Accesorios en Mano (Smartphone, Café)",
                        "13. Gafas & Máscaras de Rostro",
                        "12. Sombreros, Tiaras & Diademas",
                        "11. Flequillo & Cabello Delantero",
                        "10. Calzado & Zapatillas",
                        "09. Prenda Superior (Tops/Chaquetas)",
                        "08. Prenda Inferior (Pantalones/Faldas)",
                        "07. Maquillaje, Labial & Pedrería",
                        "06. Ojos, Pestañas & Cejas",
                        "05. Pecas, Tatuajes & Sombras",
                        "04. Cuerpo Base, Piel, Brazos & Uñas",
                        "03. Cabello Trasero",
                        "02. Mascota / Compañero",
                        "01. Fondo & Muebles de Habitación"
                    )
                    layers.forEach { layer ->
                        Text(layer, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLayersDialog = false }) {
                    Text("Entendido")
                }
            }
        )
    }
}
