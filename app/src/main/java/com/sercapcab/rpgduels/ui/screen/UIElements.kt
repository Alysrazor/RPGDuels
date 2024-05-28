package com.sercapcab.rpgduels.ui.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.sercapcab.rpgduels.R

val fontFamily = FontFamily(Font(R.font.ancient_modern_tales, FontWeight.Normal))

val stoneColor = Color(0xFF9E9486)
val mossColor = Color(0xFF8A9A5B)
val borderBrush = Brush.verticalGradient(
    colors = listOf(stoneColor, mossColor),
    startY = 0f,
    endY = 100f
)

/**
 * Composable que muestra un botón con un espacio vertical alrededor de él.
 *
 * @param textResId El recurso de cadena que se mostrará en el botón.
 * @param onClick La acción que se ejecutará cuando se haga clic en el botón.
 */
@Composable
fun ButtonWithVerticalSpacer(
    @StringRes textResId: Int,
    onClick: () -> Unit) {
    Button(
        onClick = onClick,
        border = BorderStroke(2.dp, borderBrush),
        shape = CutCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray.copy(alpha = 0.25f)
        ),
        content = {
            Text(
                text = stringResource(id = textResId),
                fontFamily = fontFamily,
                fontSize = 28.sp,
                letterSpacing = 3.sp,
            )
        }
    )
    Spacer(Modifier.padding(bottom = 25.dp))
}

/**
 * Composable que muestra un botón con un espacio vertical alrededor de él.
 *
 * @param textResId El recurso de cadena que se mostrará en el botón.
 * @param onClick La acción que se ejecutará cuando se haga clic en el botón.
 */
@Composable
fun ButtonWithHorizontalSpacer(
    @StringRes textResId: Int,
    onClick: () -> Unit) {
    Button(
        onClick = onClick,
        border = BorderStroke(2.dp, borderBrush),
        shape = CutCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray.copy(alpha = 0.25f)
        ),
        content = {
            Text(
                text = stringResource(id = textResId),
                fontFamily = fontFamily,
                fontSize = 28.sp,
                letterSpacing = 3.sp,
            )
        }
    )
    Spacer(Modifier.padding(end = 50.dp))
}

/**
 * Composable que muestra un botón con un borde alrededor de él.
 *
 * @param text El texto que se mostrará en el botón.
 * @param border El borde que se aplicará alrededor del botón.
 * @param onClick La acción que se ejecutará cuando se haga clic en el botón.
 */
@Composable
fun SpellButton(
    text: String,
    border: BorderStroke = BorderStroke(2.dp, borderBrush),
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .widthIn(min = 100.dp, max = 150.dp)
            .height(height = 50.dp),
        border = border,
        shape = CutCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray.copy(alpha = 0.25f)
        ),
        content = {
            Text(
                text = text,
                fontFamily = fontFamily,
                fontSize = 20.sp,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

/**
 * Composable que muestra un texto con un borde alrededor de él.
 *
 * @param textId El recurso de cadena que se mostrará como texto.
 * @param border El borde que se aplicará alrededor del texto.
 * @param paddingValues Los valores de padding que se aplicarán alrededor del texto.
 */
@Composable
fun TextWithBorder(
    @StringRes textId: Int,
    border: BorderStroke,
    paddingValues: PaddingValues,
    textStyle: TextStyle
) {
    Text(
        text = stringResource(id = textId),
        modifier = Modifier
            .border(border = border)
            .padding(paddingValues),
        style = textStyle
    )
}

/**
 * Composable que muestra un texto con un borde alrededor de él.
 *
 * @param modifier El modificador que se aplicará al texto.
 * @param textId El recurso de cadena que se mostrará como texto.
 * @param textAlign El alineamiento del texto.
 * @param textStyle El estilo del texto.
 */
@Composable
fun TextComposable(
    modifier: Modifier = Modifier,
    @StringRes textId: Int,
    textAlign: TextAlign = TextAlign.Center,
    textStyle: TextStyle = TextStyle(
        fontFamily = fontFamily,
        fontSize = 32.sp,
        color = Color.White
    )
) {
    Text(
        text = stringResource(id = textId),
        textAlign = textAlign,
        modifier = modifier,
        style = textStyle
    )
}

/**
 * Un botón de icono que se puede personalizar.
 *
 * @param width la anchura del icono
 * @param height la altura del icono
 * @param paddingValues los padding del icono
 * @param drawableId el recurso drawable
 * @param stringId el string en el idioma correspondiente
 */
@Composable
fun IconButtonElement(
    width: Dp = 60.dp,
    height: Dp = 60.dp,
    paddingValues: PaddingValues = PaddingValues(start = 16.dp),
    onClick: () -> Unit,
    @DrawableRes drawableId: Int,
    @StringRes stringId: Int
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .background(color = Color.Transparent, shape = RectangleShape)
            .padding(paddingValues)
            .size(width = width, height = height),
        content = {
            Column(
                modifier = Modifier.size(width = width, height = height),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Image(
                        painter = painterResource(id = drawableId),
                        contentDescription = "Icono",
                        modifier = Modifier
                            .size(width = 60.dp, height = 40.dp)
                            .padding()
                    )
                    Text(
                        text = stringResource(id = stringId),
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    )
                }
            )
        }
    )
}

/**
 * Composable que muestra una imagen que puede ser cliqueable y un texto.
 *
 * @param painter El objeto Painter que representa la imagen a mostrar en el botón.
 * @param onClick La acción que se ejecutará cuando se haga clic en el botón.
 * @param textId El recurso de cadena que se mostrará junto a la imagen.
 * @param textColor El color del texto.
 * @param textSize El tamaño del texto
 */
@Composable
fun ImageButton(
    painter: Painter,
    onClick: () -> Unit,
    @StringRes textId: Int,
    textColor: Color,
    textSize: TextUnit

) {
    Column(
        modifier = Modifier
            .height(175.dp)
            .width(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Image(
                modifier = Modifier
                    .weight(2.0f)
                    .clickable { onClick() },
                painter = painter,
                contentDescription = null
            )
            Text(
                text = stringResource(id = textId),
                modifier = Modifier.weight(1.0f),
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontSize = textSize,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            )
        }
    )
}

/**
 * Composable que muestra un checkbox con un borde alrededor de él.
 */
@Composable
fun CheckBoxWithBorder() {
    val checkedState = remember { mutableStateOf(false) }

    Checkbox(checked = checkedState.value,
        onCheckedChange = {
            checkedState.value = it
        }
    )
}

@Composable
fun editText(labelText: String, password: Boolean = false): String {
    var textValue by remember {
        mutableStateOf(TextFieldValue())
    }

    TextField(
        value = textValue,
        onValueChange = { textValue = it },
        label = { Text(labelText) },
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp),
        visualTransformation =
        if (password)
            PasswordVisualTransformation()
        else
            VisualTransformation.None
    )

    return textValue.text
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorAlertDialog(
    onDismissRequest: () -> Unit,
    @StringRes dialogTitle: Int,
    @StringRes dialogText: Int,
) {
    BasicAlertDialog(
        onDismissRequest = { onDismissRequest() },
        modifier = Modifier
            .border(brush = borderBrush, width = 4.dp, shape = CutCornerShape(4.dp))
            .background(
                color = Color.White.copy(alpha = 0.85f),
                shape = CutCornerShape(size = 4.dp)
            ),
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Image(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Icono Warning",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                    Text(
                        text = stringResource(id = dialogTitle),
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontSize = 32.sp,
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = dialogText),
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontSize = 20.sp,
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ButtonWithVerticalSpacer(
                        onClick = onDismissRequest,
                        textResId = R.string.exit)
                }
            )
        }
    )
}
